import com.example.estoque.controller.EstoqueController;
import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.awt.*;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueControllerTest{


    @Mock
    private ProdutoService produtoService; // Mock do Service

    @InjectMocks
    private EstoqueController estoqueController; // Classe a ser testada

    // ---- Testes para cadastraProduto() ----
    @Test
    public void testCadastraProduto_Success() {
        Produto produto = new Produto("Caderno", 10);
        ResponseEntity<String> response = estoqueController.cadastraProduto(produto);

        assertEquals("Cadastrado com Sucesso", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(produtoService, times(1)).cadastrarProduto(produto);
    }

    // ---- Testes para listarProdutos() ----
    @Test
    public void testListarProdutos_Success() {
        List<Produto> produtosMock = Arrays.asList(
                new Produto("Caderno", 10),
                new Produto("Caneta", 20)
        );
        when(produtoService.encontrarTodos()).thenReturn(produtosMock);

        ResponseEntity<List<Produto>> response = estoqueController.listarProdutos();

        assertEquals(2, response.getBody().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(produtoService, times(1)).encontrarTodos();
    }

    // ---- Testes para buscaProduto() ----
    @Test
    public void testBuscaProduto_Success() {
        Produto produtoMock = new Produto("Caderno", 10);
        when(produtoService.encontrarPorNome("Caneta")).thenReturn(produtoMock);

        ResponseEntity<Produto> response = estoqueController.buscaProduto("Notebook");

        assertEquals("Notebook", response.getBody().getNome());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(produtoService, times(1)).encontrarPorNome("Notebook");
    }

    // ---- Testes para atualizarEstoque() ----
    @Test
    public void testAtualizarEstoque_Success() {
        Pedido pedido = new Pedido("Caderno", 2);
        ResponseEntity<String> response = estoqueController.atualizarEstoque(pedido);

        assertEquals("Estoque Atualizado", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(produtoService, times(1)).atualizarEstoque(pedido);
    }

    @Test
    public void testAtualizarEstoque_ForaDeEstoqueException() {
        Pedido pedido = new Pedido("Caderno", 100);
        when(produtoService.atualizarEstoque(pedido))
                .thenThrow(new ForaDeEstoqueException("Quantidade indisponível"));

        ResponseEntity<String> response = estoqueController.atualizarEstoque(pedido);

        assertEquals("Quantidade indisponível", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(produtoService, times(1)).atualizarEstoque(pedido);
    }
}