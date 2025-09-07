import java.math.BigDecimal

data class ItemCardapio (
    var nome: String,
    var descricao: String,
    var preco: BigDecimal,
    var estoque: Int,
    var codigo: Int
)

enum class StatusPedido {
    ACEITO,
    FAZENDO,
    FEITO,
    ESPERANDO_ENTREGADOR,
    SAIU_PARA_ENTREGA,
    ENTREGUE
}

data class Pedido (
    var numeroPedido: Int,
    val itens: MutableList<ItemCardapio>,
    var pagamento: String,
    var status: StatusPedido,
    var valor: BigDecimal
)

val cardapio = mutableListOf<ItemCardapio>()

fun cadastrarItemAoCardapio(nomeItem: String, descricaoItem: String, precoItem: BigDecimal, estoqueItem: Int) {
    val novoItemCardapio = ItemCardapio(nome = nomeItem, descricao = descricaoItem, preco = precoItem, estoque = estoqueItem,
        codigo = cardapio.size + 1)

    cardapio.add(novoItemCardapio)
}
