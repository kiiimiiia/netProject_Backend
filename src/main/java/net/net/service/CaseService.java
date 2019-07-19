package net.net.service;


import net.net.entity.Cases;
import net.net.entity.Paraf;
import net.net.entity.User;
import net.net.entity.report;
import net.net.repository.CaseRepository;
import net.net.repository.ParafRepository;
import net.net.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CaseService {
    private CaseRepository caseRepository;
    private ParafRepository parafRepository;
    private UserRepository userRepository;

    @Autowired
    public CaseService(CaseRepository caseRepository, ParafRepository parafRepository, UserRepository userRepository) {
        this.caseRepository = caseRepository;
        this.parafRepository = parafRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void setSatisfactionForaCase(long caseId , int sat){
        Cases Case = caseRepository.get(caseId);
        Case.setSatisfaction(sat);
        caseRepository.saveOrUpdate(Case);
    }


    @Transactional
    public Cases RegisterCase(Cases newCases) {
        Cases savedCases = caseRepository.save(newCases);
        return savedCases;
    }

    public long getCountOfCases (long id)
    {
        List<Cases> cases = caseRepository.getCasesByUser(id);
        return cases.size();
    }

    public List<Cases> getAllCasesForUser(long id)
    {
        List<Cases> cases = caseRepository.getCasesByUser(id);
        return cases;
    }

    public List<Cases> getReferCase (long id)
    {
        List<Cases> cases = caseRepository.getReferCase(id);
        return cases;
    }

    public Cases getCase (long id)
    {
        Cases cases = caseRepository.get(id);
        return cases;
    }

    @Transactional
    public void ChangeCase (long userId, long id)
    {
        Cases cases = caseRepository.get(id);
        cases.setReceiverId(userId);
        caseRepository.saveOrUpdate(cases);
    }

    @Transactional
    public void ChangeCaseStatus (long id,long status)
    {
        Cases cases = caseRepository.get(id);
        cases.setStatus(status);
        caseRepository.saveOrUpdate(cases);
    }

    @Transactional
    public void SaveParaf (Paraf paraf)
    {
        parafRepository.saveOrUpdate(paraf);
    }

    public List<Paraf> getAllParaf (long id)
    {
        List<Paraf> parafs = parafRepository.getAllParafForCase(id);
        return parafs;
    }

    public report getReport() {
        List<User> users = userRepository.getAll();
        List<Cases> cases = caseRepository.getAll();
        report report = new report();
        int max = 0;
        String name = null;
        for (int i = 0; i < users.size(); i++)
        {
            int count = caseRepository.getCasesForUser(users.get(i).getId());
            if (count > max)
            {
                max = count;
                name = users.get(i).getName();
            }
        }
        double average = 0;
        for (int i = 0; i < cases.size(); i++)
        {
            if (cases.get(i).getSatisfaction() != 0) {
                average += cases.get(i).getSatisfaction();
            }
        }
        report.setActiveCount(max);
        report.setActiveName(name);
        report.setNumberOfCase(cases.size());
        report.setNumberOfUser(users.size());
        report.setAverage(average / cases.size());
        return report;

    }
}
