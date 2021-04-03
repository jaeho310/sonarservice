package com.jaeho.sonarservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class UnzipService {

    /**
     * 압축풀기 메서드
     * @param zipFileName zip파일이름
     * @param userId 사용자Uid
     * @throws Throwable
     */
    public void decompress(String zipFileName, String userId) throws Throwable {

        String directory = "./maven_files/" + userId +"/";
        File zipFile = new File(directory + zipFileName);
        directory += zipFileName.replace(".zip","");
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zipentry = null;
        // 파일 스트림
        fis = new FileInputStream(zipFile);
        // Zip 파일 스트림
        zis = new ZipInputStream(fis, Charset.forName("EUC-KR"));
        //압축되어 있는 ZIP 파일의 목록 조회
        while ((zipentry = zis.getNextEntry()) != null) {
            String filename = zipentry.getName();
            File file = new File(directory, filename);
            // entiry가 폴더면 폴더 생성
            if (zipentry.isDirectory()) {
                System.out.println("zipentry가 디렉토리입니다.");
                file.mkdirs();
            } else { //파일이면 파일 만들기
                try {
                    createFile(file, zis);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        log.info("압축풀기 성공");
    }

    private void createFile(File file, ZipInputStream zis) throws Throwable {
        File parentDir = new File(file.getParent());
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[256];
            int size = 0;
            while ((size = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, size);
            }
        } catch (Throwable e) {
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (
                        IOException e) {
                }
            }
        }
    }

}
