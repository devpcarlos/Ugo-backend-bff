package com.ugo.services;

import com.ugo.dto.ReserveDTO;
import com.ugo.dto.StateDTO;
import com.ugo.entitys.Reserve;
import com.ugo.entitys.State;
import com.ugo.entitys.User;
import com.ugo.exceptions.ReserveException;
import com.ugo.repository.IReserveRepository;
import com.ugo.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReserveServiceIMPL implements IReserveService {
    @Autowired
    UserService US;
    @Autowired
    IReserveRepository ReserveRepo;
    @Autowired
    StateServiceImpl SS;

    @Override
    public List<ReserveDTO>FindAll(){
        List<ReserveDTO>ReserveDtoList = ReserveRepo.findAll()
                .stream()
                .map(Reserve -> ReserveDTO.builder()
                        .Id(Reserve.getId())
                        .CreationDate(Reserve.getCreationDate())
                        .ReserveOwner(Reserve.getReserveOwner().getId())
                        .Currency(Reserve.getCurrency())
                        .Duration(Reserve.getDuration())
                        .floor(Reserve.getFloor())
                        .state(Reserve.getState().getId())
                        .experience(Reserve.getExperience())
                        .build())
                .toList();
                return ReserveDtoList;
    }

    @Override
    public ReserveDTO FindById(Long Id){
         Optional<Reserve>reserveOptional = ReserveRepo.findById(Id);
         if (reserveOptional.isPresent()){
             Reserve reserve = reserveOptional.get();
             ReserveDTO reserveDTO = ReserveDTO.builder()
                     .Id(reserve.getId())
                     .ReserveOwner(reserve.getReserveOwner().getId())
                     .CreationDate(reserve.getCreationDate())
                     .Duration(reserve.getDuration())
                     .Currency(reserve.getCurrency())
                     .floor(reserve.getFloor())
                     .state(reserve.getState().getId())
                     .experience(reserve.getExperience())
                     .build();
             return reserveDTO;
         }
        else {
            throw new ReserveException(404,"Incorrect Id");
         }
    }

    @Override
    public void Save(ReserveDTO reserveDTO, HttpServletRequest request){
        State state = SS.findById(reserveDTO.getState());
        User user = US.findById(reserveDTO.getReserveOwner());
        if(
                reserveDTO.getCurrency().isBlank() &&
                        reserveDTO.getReserveOwner()==null &&
                        reserveDTO.getState()==null &&
                        reserveDTO.getDuration()==0
        ){throw new ReserveException(400,"Incorrect status");}
            else {

                ReserveRepo.save(Reserve.builder()
                            .Duration(reserveDTO.getDuration())
                            .CreationDate(new Date(System.currentTimeMillis()))
                            .UpdateDate(new Date(System.currentTimeMillis()))
                            .ReserveOwner(user)
                            .state(state)
                            .floor(reserveDTO.getFloor())
                            .Currency(reserveDTO.getCurrency())
                            .experience(reserveDTO.getExperience())
                    .build()
                );
            }
        }
    @Override
    public void Update(Long Id,ReserveDTO reserveDTO){
        State state = SS.findById(reserveDTO.getState());
        User user = US.findById(reserveDTO.getReserveOwner());
        Optional<Reserve>reserveOptional=ReserveRepo.findById(Id);
        if(reserveOptional.isPresent())
        {
            ReserveRepo.save(Reserve.builder()
                    .Duration(reserveDTO.getDuration())
                    .CreationDate(new Date(System.currentTimeMillis()))
                    .UpdateDate(new Date(System.currentTimeMillis()))
                    .ReserveOwner(user)
                    .state(state)
                    .floor(reserveDTO.getFloor())
                    .Currency(reserveDTO.getCurrency())
                    .experience(reserveDTO.getExperience())
                    .build());
            }
        else {
            throw new ReserveException(404,"Update unaccomplished");
        }

    }
    @Override
    public void DeleteById(Long Id){
        if(Id!=null){ReserveRepo.deleteById(Id);}
        else {
            throw new ReserveException(404,"Incorrect Id");
        }
    }
}



