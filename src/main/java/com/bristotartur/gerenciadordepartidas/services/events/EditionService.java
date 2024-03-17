package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestEditionDto;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.ConflictException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;
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

    public Edition saveEdition(RequestEditionDto requestEditionDto) {

        var edition = editionRepository.save(editionMapper.toNewEdition(requestEditionDto));

        log.info("Edition '{}' was created.", edition.getId());
        return edition;
    }

    public void deleteEditionById(Long id) {

        var edition = this.findEditionById(id);

        if (!edition.getEditionStatus().equals(Status.SCHEDULED)) {
            throw new UnprocessableEntityException("Uma edição só pode ser excluída antes de ser iniciada.");
        }
        editionRepository.deleteById(id);
        log.info("Edition '{}' was deleted.", id);
    }

    public Edition replaceEdition(Long id, RequestEditionDto requestEditionDto) {

        var originalEdition = this.findEditionById(id);
        var updatedEdition = editionRepository.save(editionMapper.toExistingEdition(id, requestEditionDto, originalEdition));

        log.info("Edition '{}' was updated", id);
        return updatedEdition;
    }

    public Edition updateEditionStatus(Long id, Status newStatus) {

        var edition = this.findEditionById(id);
        Status.checkStatus(edition.getEditionStatus(), newStatus);

        Optional<Edition> alreadyInProgressEdition = editionRepository.findByEditionStatus(newStatus);

        if (newStatus.equals(Status.IN_PROGRESS) && alreadyInProgressEdition.isPresent()) {
            if (!edition.equals(alreadyInProgressEdition.get())) {
                throw new ConflictException("Apenas uma edição de cada pode ter o status 'IN_PROGRESS'.");
            }
        }
        edition.setEditionStatus(newStatus);
        var updatedEdition = editionRepository.save(edition);

        log.info("Edition '{}' had the status updated to '{}'.", id, newStatus);
        return updatedEdition;
    }

    public void checkEditionStatusById(Long id) {

        var edition = this.findEditionById(id);
        var status = edition.getEditionStatus();

        if (status.equals(Status.ENDED)) {
            throw new UnprocessableEntityException("Operações não podem ser realizadas em edições já encerradas.");
        }
    }

}
