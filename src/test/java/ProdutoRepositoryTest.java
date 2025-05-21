import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProdutoRepositoryTest {
    @Autowired
    private TestEntityManager entityManager; // Simula o JPA EntityManager

    @Autowired
    private ProdutoRepository produtoRepository; // Classe a ser testada

    @Test
    public void testFindByNome_WhenProdutoExists_ReturnsProduto() {
        // DADO: Um produto salvo no banco
        ProdutoEntity produto = new ProdutoEntity();
        produto.setNome("Caderno");
        produto.setQuantidade(10);
        entityManager.persist(produto);
        entityManager.flush();

        // QUANDO: Buscamos pelo nome
        ProdutoEntity encontrado = produtoRepository.findByNome("Notebook");

        // ENTÃO: O produto deve ser retornado
        assertThat(encontrado.getNome()).isEqualTo("Notebook");
        assertThat(encontrado.getQtd()).isEqualTo(10);
    }

    @Test
    public void testFindByNome_WhenProdutoNotExists_ReturnsNull() {
        // QUANDO: Buscamos um nome que não existe
        ProdutoEntity encontrado = produtoRepository.findByNome("Inexistente");

        // ENTÃO: Deve retornar null
        assertThat(encontrado).isNull();
    }
}


