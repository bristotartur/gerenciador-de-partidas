package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.EditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingEditionDto;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.EditionMapper;
import com.bristotartur.gerenciadordepartidas.repositories.EditionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EditionService {

    private final EditionRepository editionRepository;
    private final EditionMapper editionMapper;

    public Page<Edition> findAllEditions(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var editions = editionRepository.findAll(pageable);

        log.info("Edition page of number '{}' and size '{}' was returned.", number, size);
        return editions;
    }

    public Edition findEditionById(Long id) {

        var edition = editionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Edição não encontrada."));

        log.info("Edition '{}' was found.", id);
        return edition;
    }

    public ExposingEditionDto createExposingEditionDto(Edition edition) {
        return editionMapper.toNewExposingEditionDto(edition);
    }

    public Edition saveEdition(EditionDto editionDto) {

        if (!editionDto.editionStatus().equals(Status.SCHEDULED))
            throw new BadRequestException("Novas partidas só podem ser criadas com o status 'SCHEDULED'.");

        var edition = editionRepository.save(editionMapper.toNewEdition(editionDto));

        log.info("Edition '{}' was created.", edition.getId());
        return edition;
    }

    public void deleteEditionById(Long id) {

        var edition = this.findEditionById(id);

        if (!edition.getEditionStatus().equals(Status.SCHEDULED))
            throw new BadRequestException("Uma edição só pode ser excluída antes de ser iniciada.");

        editionRepository.deleteById(id);
        log.info("Edition '{}' was deleted.", id);
    }

    public Edition replaceEdition(Long id, EditionDto editionDto) {

        var originalEdition = this.findEditionById(id);
        var newStatus = editionDto.editionStatus();
        Status.checkStatus(originalEdition.getEditionStatus(), newStatus);

        Optional<Edition> alreadyInProgressEdition = editionRepository.findByEditionStatus(newStatus);

        if (newStatus.equals(Status.IN_PROGRESS) && alreadyInProgressEdition.isPresent())
            throw new BadRequestException("Apenas uma edição de cada pode ter o status 'IN_PROGRESS'.");

        var updatedEdition = editionRepository.save(editionMapper.toExistingEdition(id, editionDto, originalEdition));

        log.info("Edition '{}' was updated", id);
        return updatedEdition;
    }

}
