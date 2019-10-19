package com;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class Methods extends JFrame{
	
	//MainFrame
	private JPanel contentPane;
	int PC_VERSION=0;
	int CLOUD_VERSION=0;
	JLabel lblAtualizaoXx;
	Methods m;
	JProgressBar progressBar = null;
	JButton atualizar;
	File raiz;
	File versionFile;
	JLabel lblpc;
	JLabel lblTotalSize;
	JLabel lblnumber;
	
	
	//Methods
	static String[] text; 
	long downloaded = 0;
	public Methods() {
		
		
		//Methods
		Methods.text = getFullString();
	}
	long fileSize = 0;
	public void init() {
		//Main frame
				setTitle("Updater by Rillis");
				
				
				int diff = CLOUD_VERSION - PC_VERSION;
				
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setBounds(100, 100, 443, 267);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
				setContentPane(contentPane);
				contentPane.setLayout(null);
				
				progressBar = new JProgressBar();
				progressBar.setIndeterminate(false);
				progressBar.setBounds(0, 208, 426, 20);
				contentPane.add(progressBar);
				
				if(diff>0) {
					atualizar = new JButton("Atualizar");
					atualizar.setEnabled(false);
					atualizar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							atualizar.setEnabled(false);
							new Thread() {
								public void run() {
									int j=1;
									for (int i = PC_VERSION; i < CLOUD_VERSION; i++) {
										
										m.update(raiz, m.getUpdates()[i]);
										m.writeFile(versionFile, ""+m.getUpdates()[i].versionNumber);
										lblAtualizaoXx.setText("Atualiza\u00E7\u00E3o "+j+"/"+diff);
										j++;
										
										lblpc.setText((Integer.parseInt(lblpc.getText())+1)+"");
										lblnumber.setText((Integer.parseInt(lblnumber.getText())-1)+"");
									}
									progressBar.setIndeterminate(false);
									progressBar.setValue(100);
									atualizar.setText("Fechar");
									lblTotalSize.setVisible(false);
									atualizar.setEnabled(true);
									atualizar.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent arg0) {
											System.exit(0);
										}
									});
								}
							}.start();
						}
					});
					atualizar.setBounds(289, 169, 126, 33);
					contentPane.add(atualizar);
				}else {
					progressBar.setValue(100);
					
					atualizar = new JButton("Fechar");
					atualizar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							System.exit(0);
						}
					});
					atualizar.setBounds(289, 169, 126, 33);
					contentPane.add(atualizar);
				}
				
				
				lblAtualizaoXx = new JLabel("Atualiza\u00E7\u00E3o 0/"+diff);
				lblAtualizaoXx.setHorizontalAlignment(SwingConstants.CENTER);
				lblAtualizaoXx.setBounds(10, 183, 270, 14);
				
				if(diff<1) {
					lblAtualizaoXx.setVisible(false);
				}
				
				contentPane.add(lblAtualizaoXx);
				
				JLabel lblVersoInstalada = new JLabel("Vers\u00E3o Dispon\u00EDvel:");
				lblVersoInstalada.setFont(new Font("Tahoma", Font.PLAIN, 16));
				lblVersoInstalada.setBounds(34, 83, 143, 33);
				contentPane.add(lblVersoInstalada);
				
				JLabel label = new JLabel("Vers\u00E3o Instalada:");
				label.setFont(new Font("Tahoma", Font.PLAIN, 16));
				label.setBounds(34, 37, 143, 33);
				contentPane.add(label);
				
				JLabel lblcloud = new JLabel(CLOUD_VERSION+"");
				lblcloud.setFont(new Font("Tahoma", Font.PLAIN, 16));
				lblcloud.setBounds(178, 83, 143, 33);
				contentPane.add(lblcloud);
				
				lblpc = new JLabel(PC_VERSION+"");
				lblpc.setFont(new Font("Tahoma", Font.PLAIN, 16));
				lblpc.setBounds(178, 37, 143, 33);
				contentPane.add(lblpc);
				
				lblnumber = new JLabel(diff+"");
				lblnumber.setHorizontalAlignment(SwingConstants.CENTER);
				lblnumber.setFont(new Font("Tahoma", Font.PLAIN, 94));
				lblnumber.setBounds(289, 11, 126, 119);
				contentPane.add(lblnumber);
				
				JLabel lblNewLabel_1 = new JLabel("Atualiza\u00E7\u00F5es");
				lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_1.setBounds(289, 132, 126, 14);
				contentPane.add(lblNewLabel_1);
				
				lblTotalSize = new JLabel("Carregando...");
				if(diff<1) {
					lblTotalSize.setVisible(false);
				}
				lblTotalSize.setHorizontalAlignment(SwingConstants.CENTER);
				lblTotalSize.setBounds(0, 132, 286, 14);
				contentPane.add(lblTotalSize);
				
				new Thread() {
					public void run() {
						for (int i = PC_VERSION; i < CLOUD_VERSION; i++) {
							try {
								if (m.getUpdates()[i].url.startsWith("http")) {
									fileSize += getFileSize(new URL(getUpdates()[i].url));
									System.out.println(fileSize);
								}				
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							}
						}
						
						if(fileSize > 0) {
							lblTotalSize.setText(("Total size: 0/"+String.format("%.2f", ((double) fileSize)/1000000)+" mb"));
						}else {
							lblTotalSize.setText("File delete only updates.");
						}
						
						atualizar.setEnabled(true);
						System.out.println("a: "+fileSize);
					}
				}.start();
	}
	
	public Integer getActualVersion() {
		return Integer.parseInt(text[0].replace("g-version:",""));
	}
	
	public Update[] getUpdates() {
		ArrayList<Update> u = new ArrayList<Update>();
		
		int o = 1;
		
		Integer tVersion=0;
		String tUrl="";
		String[] tFiles=null;
		
		for (int i = 1; i < text.length; i++) {
			switch(o) {
				case 1:
					tVersion = Integer.parseInt(text[i].replace("*version:",""));
					o++;
					break;
				case 2:
					tUrl = text[i].replace("url:","");
					o++;
					break;
				case 3:
					tFiles = text[i].replace("FM:", "").split(";");
					//System.out.println("DEBUG : "+tVersion+"|"+ tUrl+"|"+ tFiles[0]);
					u.add(new Update(tVersion, tUrl, tFiles));
					o=1;
					break;
				default:
					return null;
			}
			
		}
		
		Update[] uA = new Update[u.size()];
		for (int i = 0; i < u.size(); i++) {
			uA[i] = u.get(i);
		}
		
		return uA;
	}
	
	private static String[] getFullString() {
		try {
            System.out.println("Setup...");
            URL url = new URL("https://pastebin.com/raw/Wudg4Tth");
             
            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
             
            ArrayList<String> al = new ArrayList<String>();
            String line;
            while ((line = in.readLine()) != null) {
            	al.add(line);
                System.out.println(al.size()-1 + "->" +line);
            }
            in.close();
            System.out.println("Done.");
            return toStringArray(al);
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
            return null;
        }
        catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
            return null;
        }
	}
	
	public static String[] toStringArray(ArrayList<String> al) {
		String[] s = new String[al.size()];
		
		for (int i = 0; i < al.size(); i++) {
			s[i]=al.get(i);
		}
		
		return s;
	}
	
	public String readFile(File versionFile) {
        String fileName = versionFile.getPath();
        String line = null;
        
        ArrayList<String> lines = new ArrayList<String>();
        
        try {
            FileReader fileReader = 
                new FileReader(fileName);
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        
    	return lines.toArray(new String[0])[0];
	}
	
	public void writeFile(File versionFile, String data) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(versionFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.print(data);
	    printWriter.close();
	}
	
	public void update(File raiz, Update update) {
		System.out.println("fazendo o update: "+update.versionNumber);
		
		//remove os arquivos
		for (int i = 0; i < update.filesToRemove.length; i++) {
			if(!update.filesToRemove[i].equals("")) {
				System.out.println("Removing "+update.filesToRemove[i]+" -> "+ delete(new File(raiz+"\\"+update.filesToRemove[i])));
			}
		}
		
		//baixar e unzip
		if(!update.url.equals("fileremoveonly")) {
			System.out.println("baixando -> "+update.url);
			
			
			File temp = new File(raiz+"/update");
			temp.mkdir();
			
			int random = (int) ((Math.random() * ((9999 - 1000) + 1)) + 1000);
			
			File updateZip = new File(temp+"/"+random+".zip");
			
			download(update.url,updateZip);			
			System.out.println(System.getenv("APPDATA") + "/Rillis/Updater/"+random+".zip -> "+updateZip.length()/1000000+ " mb");
			
			
			try {
				unzip(updateZip.getPath(), raiz.getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Removing "+"/Rillis/Updater/"+random+".zip -> "+ delete(updateZip));
			temp.delete();
		}
		//JOptionPane.showMessageDialog(null, "Jogo atualizado para a versão v"+update.versionNumber);
		
	}
	
	@SuppressWarnings("resource")
	private void download(String url, File f) {
		try {
			BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(f);
		    byte dataBuffer[] = new byte[1024];
		    int bytesRead;
		    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
		        fileOutputStream.write(dataBuffer, 0, bytesRead);
		        downloaded++;
		        lblTotalSize.setText("Total size: "+String.format("%.2f", ((double)downloaded)*1024/1000000)+"/"+String.format("%.2f", ((double) fileSize)/1000000)+" mb");
		        progressBar.setIndeterminate(false);
		        //System.out.println((int) ((downloaded*1024)/(fileSize/100)));
		        progressBar.setValue((int) ((downloaded*1024)/(fileSize/100)));
		        
		    }
		    in.close();
		    fileOutputStream.close();
		} catch (IOException e) {
		    // handle exception
		}
	}
	
	private boolean delete(File fileToDelete) {
		if(fileToDelete.exists()) {
			fileToDelete.delete();
			return true;
		}else {
			return false;
		}
	}
	
	void clearZIP(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.getName().contains(".zip")) {
	            	f.delete();
	            }
	        }
	    }
	}
	
	public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                new File(filePath).getParentFile().mkdirs();
                extractFile(zipIn, filePath);
                System.out.println(filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                System.out.println(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    static long getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }
}
