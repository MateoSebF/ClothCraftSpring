package co.edu.escuelaing.cvds.ClothCraft.service;
import co.edu.escuelaing.cvds.ClothCraft.model.Session;
import co.edu.escuelaing.cvds.ClothCraft.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session getSessionByToken(UUID token) {
        return sessionRepository.findByToken(token);
    }
}