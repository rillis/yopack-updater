package com;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Run {
	static MainFrame mF;
	public static void main(String args[]) {
		//run
		//first run?
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		Methods m = new Methods();
		boolean FIRST_RUN = false;
		File raiz = null;
		
		String cfgFolderPath = System.getenv("APPDATA") + "/Rillis/Updater";
		File cfgFolder = new File(cfgFolderPath);
		String cfgFilePath = cfgFolderPath + "/config.cfg";
		File cfgFile = new File(cfgFilePath);
		if(!cfgFile.exists()) {
			FIRST_RUN = true;
			cfgFolder.mkdirs();
			try {
				cfgFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(FIRST_RUN) {
			File selected = null;
			JOptionPane.showMessageDialog(null, "Identifiquei que é sua primeira execução, me indique a pasta do modpack. (yopack-1710)");
				while(true) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fc.showSaveDialog(null);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						selected = fc.getSelectedFile();
					}else if(returnVal == JFileChooser.CANCEL_OPTION) {
						cfgFile.delete();
						JOptionPane.showMessageDialog(null, "Cancelando");
						System.exit(0);
					}
					if(new File(selected+"/bin").exists()) {
						break;
					}else {
						JOptionPane.showMessageDialog(null, "Pasta invalida :(");
					}
				}
			m.writeFile(cfgFile, selected.getPath());
		}
		
		raiz = new File(m.readFile(cfgFile));
		
		File versionFile = new File(raiz + "\\version.rfg");
		
		if(!versionFile.exists()) {
			try {
				versionFile.createNewFile();
				m.writeFile(versionFile, "1");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Integer VERSAO = Integer.parseInt(m.readFile(versionFile));
		//Configuração inicial ok
		System.out.println("System version -> "+VERSAO);
		System.out.println("Cloud version -> "+m.getActualVersion());
		
		//VERSAO, m.getActualVersion(), m, raiz, versionFile
		
		m.PC_VERSION = VERSAO;
		m.CLOUD_VERSION = m.getActualVersion();
		m.m=m;
		m.raiz = raiz;
		m.versionFile = versionFile;
		
		m.init();
		m.setVisible(true);
		
		/**
		if(VERSAO==m.getActualVersion()) {
			JOptionPane.showMessageDialog(null, "Você está em dia com as atualizações");
			System.exit(0);
		}else {
			JOptionPane.showMessageDialog(null, "Quantidade de updates: "+(m.getActualVersion()-VERSAO));
		}
		for (int i = VERSAO; i < m.getActualVersion(); i++) {
			m.update(raiz, m.getUpdates()[i]);
			m.writeFile(versionFile, ""+m.getUpdates()[i].versionNumber);
		}
		JOptionPane.showMessageDialog(null, "Tudo pronto, pode iniciar o jogo!");
		*/
	}
}
