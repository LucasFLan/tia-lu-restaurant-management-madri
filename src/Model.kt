data class Produto (
    var nome: String,
    var descricao: String,
    var preco: Float,
    var estoque: Int,
    var codigo: Int
)

data class Pedido (
    var numeroPedido: Int,
    var itens: MutableList<Produto>,
    var pagamento: String,
    var status: OrderStatus,
    var valor: Float
)

enum class OrderStatus {
    ACEITO,
    FAZENDO,
    FEITO,
    ESPERANDO_ENTREGADOR,
    SAIU_PARA_ENTREGA,
    ENTREGUE
}

var menu = mutableListOf<Produto>()

fun adicionar_item_ao_menu(nomeItem: String, descricaoItem: String, precoItem: Float, estoqueItem: Int) {
    val novoProduto = Produto(nome = nomeItem, descricao = descricaoItem, preco = precoItem, estoque = estoqueItem,
        codigo = menu.size + 1)

    menu.add(novoProduto)
}
