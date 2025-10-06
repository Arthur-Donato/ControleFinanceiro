package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class criarNovaCategoriaService {

    @Setter
    @Getter
    private UserRepository userRepository;

    @Setter
    @Getter
    private CategoryRepository categoryRepository;



}
