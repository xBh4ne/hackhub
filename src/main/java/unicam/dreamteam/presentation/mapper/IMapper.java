package unicam.dreamteam.presentation.mapper;

public interface IMapper<O, D>{
    D toDTO(O Object);
}
