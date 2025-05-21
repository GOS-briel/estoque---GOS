import com.example.estoque.domain.ItemPedido;
import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.repository.ProdutoRepository;
import com.example.estoque.service.ProdutoService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.example.estoque.service.ProdutoService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class ProdutoServiceTest {

private ProdutoServiceTest repository;
private ProdutoService service;
    private void assertEquals(String monitor, String nome) {
    }

@BeforeEach
    void setUP(){
    repository = mock(ProdutoRepository.class);
    service = new ProdutoService(repository);
}
@Test
    void testCadastrarProdutoNovo(){
    Produto produto = new Produto();
    produto.setNome("Caderno");
    produto.setQtd(50);
    when(repository.findByNome("Caderno")).thenReturn(null);

    service.cadastrarProduto(produto);
   verify(repository).save(any(ProdutoRepository.class));

}

@Test
    void testCadastrarProdutoExistente() {
    ProdutoEntity existente = new ProdutoEntity();
    existente.setNome("Caderno");
    existente.setQtd(10);

    Produto produto = new Produto();
    produto.setNome("Caderno");
    produto.setQtd(20);

    when(repository.findByNome("Caderno")).thenReturn(existente);
    service.cadastrarProduto(produto);
    verify(repository).save(existente);

    assertEquals(20, existente.getQtd());
    when(repository.findByNome("Caderno")).thenReturn(existente);
    verify(repository).save(existente);
    assertEquals(20, existente.getQtd());

}
@Test
void testEncontrarTodos() {
    ProdutoEntity produto1 = new ProdutoEntity();
    produto1.setNome("Item1");

    ProdutoEntity produto2 = new ProdutoEntity();
    produto2.setNome("Item2");

    when(repository.findALL()).thenReturn(Arrays.asList(produto1, produto2));

    List<Produto> produtos = service.encontrarTodos();
    assertEquals(2, produtos.size());
}
  @Test
          void testAtualizarEstoqueComSucesso() {

      ProdutoEntity produtoEntity = new ProdutoEntity();
      produtoEntity.setId(1L);
      produtoEntity.setNome("Caneta");
      produtoEntity.setQtd(10);

      ItemPedido item = new ItemPedido();
      item.setId(1L);
      item.setQtd(5);

      Pedido pedido = new Pedido();
      pedido.setItens(Collections.singletonList(item));

      when(repository.findByid(1L)).thenReturn(Optional.of(produtoEntity));

      service.atualizarEstoque(pedido);
      assertEquals(5, produtoEntity.getQtd());
      verify(repository).save(produtoEntity);

  }

  @Test
void testAtualizarEstoqueComerro(){
ProdutoEntity produtoEntity = new ProdutoEntity();
produtoEntity.setId(1L);
produtoEntity.setNome("Mouse");
produtoEntity.setQtd(3);

ItemPedido itemPedido = new ItemPedido();
itemPedido.setId( 1L );
itemPedido.setQtd(5);

Pedido pedido = new Pedido();
pedido.setItens(Collections.singletonList(itemPedido));

when(repository.findById(1L)).thenReturn(Optional.of(produtoEntity));
service.atualizarEstoque(pedido);

assertEquals(5 ,produtoEntity.getQtd());
verify(repository).save(produtoEntity);

}

    @Test
    void testAtualizarEstoqueComErro() {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(1L);
        produtoEntity.setNome("Mouse");
        produtoEntity.setQtd(3);

        ItemPedido itemPedido = new ItemPedido();

        Pedido pedido = new Pedido();
        pedido.setItens(Collections.singletonList(itemPedido));

        when(repository.findById(1L)).thenReturn(Optional.of(produtoEntity));

        ForaDeEstoqueException ex = assertThrows(ForaDeEstoqueException.class, () -> {
            service.atualizarEstoque(pedido);
        });

       assertTrue(ex.getMessage().contains("Produto Mouse possui apenas"));
       verify(repository,never()).save(any());
}
@Test
void testEncontrarPorNome(){
    ProdutoEntity entity = new ProdutoEntity();
    entity.setNome("Monitor");

    when(repository.findByNome("Monitor")).thenReturn(entity);

    Produto result = service.encontrarPorNome("Monitor");

    assertEquals("Monitor", result.getNome());
}


}
