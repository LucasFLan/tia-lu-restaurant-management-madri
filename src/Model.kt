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
