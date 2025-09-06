import main
import java.math.BigDecimal

data class ItemCardapio (
    var nome: String,
    var descricao: String,
    var preco: BigDecimal,
    var estoque: Int,
    var codigo: Int
)

data class Pedido (
    var numeroPedido: Int,
    val itens: MutableList<ItemCardapio>,
    var pagamento: String,
    var status: Status,
    var valor: BigDecimal
)

enum class Status {
    ACEITO,
    FAZENDO,
    FEITO,
    ESPERANDO_ENTREGADOR,
    SAIU_PARA_ENTREGA,
    ENTREGUE
}

val cardapio = mutableListOf<ItemCardapio>()

fun adicionar_item_ao_cardapio(nomeItem: String, descricaoItem: String, precoItem: BigDecimal, estoqueItem: Int) {
    val novoItemCardapio = ItemCardapio(nome = nomeItem, descricao = descricaoItem, preco = precoItem, estoque = estoqueItem,
        codigo = cardapio.size + 1)

    cardapio.add(novoItemCardapio)
}
