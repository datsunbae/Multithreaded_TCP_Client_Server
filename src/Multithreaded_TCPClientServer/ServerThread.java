/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Multithreaded_TCPClientServer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
/**
 *
 * @author datsu
 */
public class ServerThread implements Runnable{
    private Scanner in = null;
    private PrintWriter out = null;
    private Socket socket;
    private String name;


    public ServerThread(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String chuoi = in.nextLine().trim(); 
                System.out.println("Chuoi nhan: " + chuoi);
                int flag = Integer.parseInt(chuoi.split(" ")[0].toString());
                
                System.out.println("Co la: " + flag);
                if(flag==1) // Register
                {
                    String userName = chuoi.split(" ")[1].trim();
                    System.out.println("Username: " + userName);
                    String password = chuoi.split(" ")[2].trim();
                     System.out.println("Password: " + password);
                    
                    
                    DBAccess acc = new DBAccess();
                    ResultSet rs = acc.Query("select * from ACCOUNT where username = '"+userName+"'");
                    if(rs.next()){
                        System.out.println("Tài khoản đã tồn tại");
                        out.println("Tài khoản đã tồn tại");
                    } else {
                       int kq = acc.Update("insert into ACCOUNT(Username, Password) values('"+userName+"','"+password+"')");
                        if(kq!=0){
                            out.println("Đăng ký tài khoản thành công");
                        } else {
                            out.println("Đăng ký tài khoản thất bại");
                        }
                    }

                }
                else if (flag == 2) //Login
                {
                    String userName = chuoi.split(" ")[1].trim();
                    System.out.println("Username: " + userName);
                    String password = chuoi.split(" ")[2].trim();
                     System.out.println("Password: " + password);
                    
                    DBAccess acc = new DBAccess();
                    ResultSet rs = acc.Query("select * from ACCOUNT where username = '"+userName+"' and password = '"+password+"'");
                    if(rs.next()){
                        out.println(1);
                    } else {
                        out.println(0);
                    }
                }
                
                else if(flag == 3){
                    String patch = chuoi.split(" ")[1].trim();
                    System.out.println("Đường dẫn là: " + patch);
                    
                    if(KiemTraFileTonTai(patch)){
                        int[][] maTran = getMaTran(patch);
                    
                        int[] dongCot = getDongCot(patch);
                        int dong = dongCot[0];
                        int cot = dongCot[1];

                        
                        int xuatPhat = maTran[0][0];
                        int Dich = maTran[dong - 1][cot - 1];
                        int dongHienTai = 0;
                        int cotHienTai = 0;
                        int fCost = 0;
                        String kq = "";

                        for(int i =0;i<3;i++){
                            int cost = maTran[0][0];
                            String ketqua = "";
                            ketqua =  maTran[dongHienTai][cotHienTai] + " + ";
                            
                            switch(i){
                                case 0: //duong cheo chinh
                                    dongHienTai = 1;
                                    cotHienTai = 1;
                                    ketqua += maTran[dongHienTai][cotHienTai] + " + ";
                                    cost+= maTran[dongHienTai][cotHienTai];
                                    break;
                                case 1: // qua phai
                                    dongHienTai = 0;
                                    cotHienTai = 1;
                                    ketqua += maTran[dongHienTai][cotHienTai] + " + ";
                                    cost+= maTran[dongHienTai][cotHienTai];
                                    break;
                                case 2: // xuong
                                    dongHienTai = 1;
                                    cotHienTai = 0;
                                    ketqua += maTran[dongHienTai][cotHienTai] + " + ";
                                    cost+= maTran[dongHienTai][cotHienTai];
                                    break;
                            }
                            
                            while(!KiemTraDich(dongHienTai, cotHienTai, dong, cot)){
                                int[] result = CheckDuongDi(dong, cot, maTran, dongHienTai, cotHienTai, cost);
                                dongHienTai = result[0];
                                cotHienTai = result[1];
                                cost += maTran[dongHienTai][cotHienTai];
                                ketqua +=  maTran[dongHienTai][cotHienTai] + " + ";
                            }
                            
                           
                            if(i == 0){
                                fCost = cost;
                                ketqua += "= " + cost;
                                kq = ketqua;
                            }

                            if(i >=1 && (cost + maTran[dong-1][cot-1]) < fCost){
                                fCost = cost;
                                ketqua += "= " + cost;
                                kq = ketqua;
                            }
                            dongHienTai = 0;
                            cotHienTai = 0;
                        }
  
                        out.println(kq);
                    }
                    else{
                        out.println("File không tồn tại");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(name + " has departed");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
    
    public boolean KiemTraFileTonTai(String patch){
        boolean check = false;
        File file = new File(patch);
        if(file.exists()){
            check = true;
        }
            
        return check;
    }
    
    public int[][] getMaTran(String patch){
        int[][] maTran = null;
        int dong = 0;
        int cot = 0;
        File file = new File(patch);
        try {
            Scanner scanner = new Scanner(file);
            int mMaxtrix = Integer.parseInt(scanner.nextLine().toString());
            int nMatrix = Integer.parseInt(scanner.nextLine().toString());
            dong = mMaxtrix;
            cot = nMatrix;
            maTran = new int[mMaxtrix][nMatrix];
            for (int i = 0; i < mMaxtrix; i++) {
                String[] numbers = scanner.nextLine().split(" ");
                for (int j = 0; j < nMatrix; j++) {
                    maTran[i][j] = Integer.parseInt(numbers[j]);
                }
            }
            
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return maTran;
    }
    
    public int[] getDongCot(String patch){
        int[] dongCot = new int[2];
        File file = new File(patch);
        try {
            Scanner scanner = new Scanner(file);
            int mMaxtrix = Integer.parseInt(scanner.nextLine().toString());
            int nMatrix = Integer.parseInt(scanner.nextLine().toString());
            dongCot[0] = mMaxtrix;
            dongCot[1] = nMatrix;
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dongCot;
    }
    
    public int[] CheckDuongDi(int soDong, int soCot, int[][] value, int dongHienTai, int cotHienTai, int cost){
        int[] viTriTiepTheo = {0,0}; 
        int phai = -1;
        int duoi = -1;
        int duongCheoChinh = -1;
        int min = 0;
        List<Integer> costList = new ArrayList<>();
        
        //Duong cheo chinh
        if(KiemTraHopLe(dongHienTai + 1, cotHienTai + 1, soDong, soCot)){
            //Dich
            if(KiemTraDich(dongHienTai + 1, cotHienTai + 1, soDong, soCot)){
                viTriTiepTheo[0] = dongHienTai + 1;
                viTriTiepTheo[1] = cotHienTai + 1;
                return viTriTiepTheo;
            }
            
            duongCheoChinh = cost + value[dongHienTai + 1][cotHienTai + 1];
            min = duongCheoChinh;
            viTriTiepTheo[0] = dongHienTai + 1;
            viTriTiepTheo[1] = cotHienTai + 1;
        }
        
        
        //Qua Phai
        if(KiemTraHopLe(dongHienTai, cotHienTai + 1, soDong, soCot)){
            
            //Dich
            if(KiemTraDich(dongHienTai, cotHienTai + 1, soDong, soCot)){
                viTriTiepTheo[0] = dongHienTai;
                viTriTiepTheo[1] = cotHienTai + 1;
                return viTriTiepTheo;
            }
            
            
            phai = cost + value[dongHienTai][cotHienTai + 1];
            
        }
        
        //Xuong duoi
        if(KiemTraHopLe(dongHienTai + 1, cotHienTai, soDong, soCot)){
            //Dich
            if(KiemTraDich(dongHienTai + 1, cotHienTai, soDong, soCot)){
                viTriTiepTheo[0] = dongHienTai + 1;
                viTriTiepTheo[1] = cotHienTai;
                return viTriTiepTheo;
            }
            
            duoi = cost + value[dongHienTai + 1][cotHienTai];
        }
        
        
        
       if(min == 0){
           
           if(duoi == -1){
               viTriTiepTheo[0] = dongHienTai;
               viTriTiepTheo[1] = cotHienTai + 1;
           }else if(phai == -1){
              viTriTiepTheo[0] = dongHienTai + 1;
              viTriTiepTheo[1] = cotHienTai; 
           }
           else{
                if(duoi > phai){
                    viTriTiepTheo[0] = dongHienTai;
                    viTriTiepTheo[1] = cotHienTai + 1;
               }
                else{
                    viTriTiepTheo[0] = dongHienTai + 1;
                    viTriTiepTheo[1] = cotHienTai;
                } 
           }
           
           
       }
       else{
           if(phai < min && phai != -1){
               min = phai;
               viTriTiepTheo[0] = dongHienTai;
               viTriTiepTheo[1] = cotHienTai + 1;
           }
           
           if(duoi < min && duoi != -1){
               viTriTiepTheo[0] = dongHienTai + 1;
               viTriTiepTheo[1] = cotHienTai;
           }
       }

        return viTriTiepTheo;
    }
    
    public boolean KiemTraHopLe(int dongCanDi, int cotCanDi, int m, int n){
        boolean check = true;
        if(dongCanDi > m - 1 || cotCanDi > n - 1){
            check = false;
        }
        return check;
    }
    
    public boolean KiemTraDich(int dongCanDi, int cotCanDi, int m, int n){
        boolean check = false;
        if(dongCanDi == m - 1 && cotCanDi == n -1){
            return true;
        }
        
        return check;
    }
    
   
}
