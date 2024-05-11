package com.fusm.programs.service.impl;

import com.fusm.programs.dto.MinuteDto;
import com.fusm.programs.entity.ProgramAttachment;
import com.fusm.programs.external.IDocumentManagerService;
import com.fusm.programs.model.GuidelineModel;
import com.fusm.programs.model.GuidelineRequest;
import com.fusm.programs.model.SearchModel;
import com.fusm.programs.repository.IProgramAttachmentRepository;
import com.fusm.programs.service.IProgramAttachmentService;
import com.fusm.programs.service.specific.ProgramAttachmentMapper;
import com.fusm.programs.util.SharedMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramAttachmentService implements IProgramAttachmentService {

    @Autowired
    private IProgramAttachmentRepository programAttachmentRepository;

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Autowired
    private SharedMethod sharedMethod;

    ProgramAttachmentMapper programAttachmentMapper = new ProgramAttachmentMapper();


    @Override
    public List<GuidelineModel> getGuidelineAttachment() {
        return programAttachmentRepository.findAllOrdered().stream().map(
                programAttachment -> GuidelineModel.builder()
                        .programAttachmentId(programAttachment.getProgramAttachmentId())
                        .name(programAttachment.getName())
                        .fileUrl(programAttachment.getFileUrl())
                        .fileType(programAttachment.getFileType())
                        .createdAt(programAttachment.getCreatedAt())
                        .createdBy(programAttachment.getCreatedBy())
                        .updatedAt(programAttachment.getUpdatedAt())
                        .enabled(programAttachment.getEnabled())
                        .attachmentFatherId(programAttachment.getProgramAttachmentId())
                        .build()
        ).toList();
    }

    @Override
    public List<GuidelineModel> getGuidelineAttachmentByType(Integer type) {
        return programAttachmentRepository.findAllOrderedByType(type).stream().map(
                programAttachment -> GuidelineModel.builder()
                        .programAttachmentId(programAttachment.getProgramAttachmentId())
                        .name(programAttachment.getName())
                        .fileUrl(programAttachment.getFileUrl())
                        .fileType(programAttachment.getFileType())
                        .createdAt(programAttachment.getCreatedAt())
                        .createdBy(programAttachment.getCreatedBy())
                        .updatedAt(programAttachment.getUpdatedAt())
                        .enabled(programAttachment.getEnabled())
                        .attachmentFatherId(programAttachment.getProgramAttachmentId())
                        .build()
        ).toList();
    }

    @Override
    public void createGuidelineAttachment(GuidelineRequest guidelineRequest) {
        Optional<ProgramAttachment> programAttachmentOptional = (guidelineRequest.getFatherId() != null) ?
                programAttachmentRepository.findById(guidelineRequest.getFatherId()) : Optional.empty();
        programAttachmentRepository.save(
                ProgramAttachment.builder()
                        .name(guidelineRequest.getFileName())
                        .fileUrl(sharedMethod.saveFile(guidelineRequest.getFile(), guidelineRequest.getCreatedBy()))
                        .orderFile(1)
                        .createdBy(guidelineRequest.getCreatedBy())
                        .createdAt(new Date())
                        .enabled(true)
                        .orderFile(guidelineRequest.getFileOrder())
                        .fileType(guidelineRequest.getFileType())
                        .attachmentFatherId(programAttachmentOptional.orElse(null))
                        .build()
        );
    }

    @Override
    public void updateGuidelineAttachment(GuidelineRequest guidelineRequest, Integer guidelineId) {
        Optional<ProgramAttachment> guidelineAttachmentOptional = programAttachmentRepository.findById(guidelineId);
        if (guidelineAttachmentOptional.isPresent()) {
            ProgramAttachment programAttachment = guidelineAttachmentOptional.get();
            if (guidelineRequest.getFile() != null) {
                programAttachment.setFileUrl(sharedMethod.saveFile(guidelineRequest.getFile(), guidelineRequest.getCreatedBy()));
            }
            programAttachment.setName(guidelineRequest.getFileName());
            programAttachment.setUpdatedAt(new Date());
            programAttachmentRepository.save(programAttachment);
        }
    }

    @Override
    public void disableGuidelineAttachment(Integer guidelineId) {
        Optional<ProgramAttachment> guidelineAttachmentOptional = programAttachmentRepository.findById(guidelineId);
        if (guidelineAttachmentOptional.isPresent()) {
            ProgramAttachment programAttachment = guidelineAttachmentOptional.get();
            programAttachment.setEnabled(false);
            programAttachmentRepository.save(programAttachment);
        }
    }

    @Override
    public List<GuidelineModel> getMinutes(SearchModel searchModel) {
        List<MinuteDto> minuteList = programAttachmentRepository
                .findAllMinutes(searchModel.getFileDate(), searchModel.getFileName());
        return programAttachmentMapper.mapToTree(minuteList, (searchModel.getFileDate() != null || searchModel.getFileName() != null));
    }

    @Override
    public void deleteGuidelineAttachment(Integer guidelineId) {
        Optional<ProgramAttachment> guidelineAttachmentOptional = programAttachmentRepository.findById(guidelineId);
        if (guidelineAttachmentOptional.isPresent()) {
            ProgramAttachment programAttachment = guidelineAttachmentOptional.get();
            documentManagerService.deleteDocumentMassive(List.of(programAttachment.getFileUrl()));
            programAttachmentRepository.delete(programAttachment);
        }
    }

    @Override
    public void enableDisableGuideline(Integer guidelineId, Boolean enabled) {
        Optional<ProgramAttachment> guidelineAttachmentOptional = programAttachmentRepository.findById(guidelineId);
        if (guidelineAttachmentOptional.isPresent()) {
            ProgramAttachment programAttachment = guidelineAttachmentOptional.get();
            programAttachment.setEnabled(enabled);
            programAttachmentRepository.save(programAttachment);
        }
    }

    @Override
    public String getGuidelineAttachmentById(Integer guidelineId) {
        String route = "";
        Optional<ProgramAttachment> guidelineAttachmentOptional = programAttachmentRepository.findById(guidelineId);
        if (guidelineAttachmentOptional.isPresent()) {
            route = guidelineAttachmentOptional.get().getFileUrl();
        }
        return route;
    }

}
