import java.math.BigDecimal
import kotlin.plus

data class ItemCardapio (
    var nome: String,
    var descricao: String,
    var preco: BigDecimal,
    var estoque: Int,
    val codigo: Int
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
    val codigo: Int,
    var pagamento: String,
    var status: StatusPedido,
    var valor: BigDecimal
)

data class PedidoItem (
    val codigoPedido: Int,
    val codigoItem: Int,
    var quantidade: Int,
)

val cardapio = mutableListOf<ItemCardapio>()
var codigoItem: Int = 1

var codigoPedido: Int = 1
val pedidos = mutableListOf<Pedido>()
val itensPedido = mutableListOf<PedidoItem>()

fun adicionarItemAoCardapio(nomeItem: String, descricaoItem: String, precoItem: BigDecimal, estoqueItem: Int) {
    val novoItemCardapio = ItemCardapio(nome = nomeItem, descricao = descricaoItem, preco = precoItem, estoque = estoqueItem,
        codigo = codigoItem++)

    cardapio.add(novoItemCardapio)
}

fun buscarItemPorCodigo(codigo: Int): ItemCardapio? {
    return cardapio.find {it.codigo == codigo}
}

fun atualizarItem(item: ItemCardapio, novoNome: String, novaDescricao: String, novoPreco: BigDecimal?, adicaoEstoque: Int?){
    if(novoNome.isNotBlank()) {
        item.nome = novoNome.trim()
    }

    if(novaDescricao.isNotBlank()) {
        item.descricao = novaDescricao.trim()
    }

    novoPreco?.let { item.preco = it }

    adicaoEstoque?.let { item.estoque += it }

}

fun criarNovoPedido(codigoPedido: Int): Pedido {
    return Pedido(codigo = codigoPedido, pagamento = "Em análise",
        valor = BigDecimal("0"), status = StatusPedido.ACEITO)
}

fun removerEstoqueProduto(pedido: Pedido): Unit {
    itensPedido.forEach { itemPedido ->
        if (itemPedido.codigoPedido == pedido.codigo) {
            val itemEscolhido = buscarItemPorCodigo(itemPedido.codigoItem)

            itemEscolhido!!.estoque -= itemPedido.quantidade

            pedido.valor += (itemEscolhido.preco * itemPedido.quantidade.toBigDecimal())

        }
    }
}

fun limparCarrinho(pedido: Pedido) {
    itensPedido.forEach { itemPedido ->
        if (itemPedido.codigoPedido ==  pedido.codigo) {
            val itensEscolhidos = cardapio.filter { it.codigo == itemPedido.codigoItem }

            itensEscolhidos.forEach { item ->
                if (item.codigo == itemPedido.codigoItem){
                    item.estoque += itemPedido.quantidade
                }
            }
        }
    }

    itensPedido.removeIf { it.codigoPedido == pedido.codigo }
}

fun verificarSeCarrinhoEstaVazio(pedido: Pedido): Boolean {
    return itensPedido.none { it.codigoPedido == pedido.codigo }
}

fun atualizarStatusPedido(pedido: Pedido, statusEscolhido: Int) {
    pedido.status = StatusPedido.entries[statusEscolhido - 1]
}
