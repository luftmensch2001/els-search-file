package com.elasticsearch.demo.servicesImpl;

import com.elasticsearch.demo.DTO.FileDTO;
import com.elasticsearch.demo.models.FileElsEntity;
import com.elasticsearch.demo.models.FileEntity;
import com.elasticsearch.demo.repositories.FileElsRepository;
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
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileElsRepository fileElsRepository;

    public final String WORD_FILE_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    @Override
    public Iterable<FileEntity> listAll() {
        return fileRepository.findAll();
    }

    @Override
    public Pair<Boolean, Object> uploadFile(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileType = file.getContentType();
//            Check file type
            if (!fileType.equals(WORD_FILE_TYPE)) {
                return Pair.of(false, "Invalid file type");
            }
//            Save file into database
            FileEntity newFile = new FileEntity(fileName, fileType, file.getBytes());
            fileRepository.save(newFile);
//            Save file info into els
            String fileContent = getFileContent(fileName, file);
            FileElsEntity newFileEls = new FileElsEntity(newFile.getId(), fileName, fileType, fileContent);
            fileElsRepository.save(newFileEls);
//            Return success
            return Pair.of(true, newFile);
        } catch (Exception e) {
            System.out.println(e);
            return Pair.of(false, "Upload file failed");
        }
    }

    @Override
    public Pair<Boolean, Object> downloadFile(Integer id) {
        Optional<FileEntity> file = fileRepository.findById(id);
        if (!file.isPresent())
            return Pair.of(false, "File not found");
        else
            return Pair.of(true, file.get());
    }

    @Override
    public Pair<Boolean, Object> findById(Integer id) {
        Optional<FileEntity> file = fileRepository.findById(id);
        if (file.isPresent())
            return Pair.of(true, file.get());
        else
            return Pair.of(false, "File not found");
    }

    @Override
    public List<FileDTO> findByContent(String content) {
        List<FileElsEntity> files = fileElsRepository.findByContent(content);
        List<FileDTO> result = new ArrayList<>();

        for (int i = 0; i<files.size(); i++) {
            FileElsEntity file = files.get(i);
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

    @Override
    public void deleteFile(Integer id) {
        fileRepository.deleteById(id);
        fileElsRepository.deleteById(id);
    }
}
