package com.elasticsearch.demo.servicesImpl;

import com.elasticsearch.demo.DTO.FileDTO;
import com.elasticsearch.demo.models.FileEntity;
import com.elasticsearch.demo.repositories.FileRepository;
import com.elasticsearch.demo.services.FileService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;

    public final String WORD_FILE_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    @Override
    public Iterable<FileEntity> listAll() {
        return fileRepository.findAll();
    }

    @Override
    public Pair<Boolean, Object> uploadFile(MultipartFile file) {
        try {
//            Get info and content of file
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileType = file.getContentType();
//            Check file type
            if (!fileType.equals(WORD_FILE_TYPE)) {
                return Pair.of(false, "Invalid file type");
            }
            String fileContent = getFileContent(fileName, file);
            FileEntity newFile = new FileEntity(fileName, fileType, fileContent);
//            Store file
            fileRepository.save(newFile);
            return Pair.of(true, newFile);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Upload file failed");
        }
    }

    @Override
    public List<FileDTO> findByContent(String content) {
        List<FileEntity> files = fileRepository.findByContent(content);
        List<FileDTO> result = new ArrayList<>();

        for (int i = 0; i<files.size(); i++) {
            FileEntity file = files.get(i);
            result.add(new FileDTO(file.getId(), file.getName(), file.getType()));
        }

        return result;
    }

    public String getFileContent(String fileName, MultipartFile file) throws IOException, InvalidFormatException {
//        Transfer multipartfile to temp file
        File tempFile = File.createTempFile(fileName, "");
        file.transferTo(tempFile);
//        Read file content
        FileInputStream fis = new FileInputStream(tempFile.getAbsolutePath());
        StringBuilder content = new StringBuilder();
        XWPFDocument document = new XWPFDocument(OPCPackage.open(fis));
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph para : paragraphs) {
            content.append(" " + para.getText());
        }
        fis.close();
        return content.toString();
    }
}
