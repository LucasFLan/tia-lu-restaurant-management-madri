import java.math.BigDecimal

fun lerInt(mensagem: String): Int {
    var entrada: Int?

    do {
        print(mensagem)
        entrada = readln().toIntOrNull()
        if(entrada == null) {
            println("Entrada inválida! Por favor, digite um número inteiro")
        } else if(entrada < 0) {
            println("Entrada inválida! Por favor, digite um número inteiro positivo")
            entrada = null
        }
    } while (entrada == null)

    return entrada
}

fun lerBigDecimal(mensagem: String): BigDecimal {
    var entrada: BigDecimal?

    do {
        print(mensagem)
        entrada = readln().toBigDecimalOrNull()
        if(entrada == null) {
            println("Entrada inválida! Por favor, digite um número")
        } else if(entrada < BigDecimal.ZERO) {
            println("Entrada inválida! Por favor, digite um número inteiro positivo")
            entrada = null
        }
    } while (entrada == null)

    return entrada
}

fun lerString(mensagem: String): String {
    var entrada: String

    do {
        print(mensagem)
        entrada = readln().trim()

        if (entrada.isEmpty()){
            println("A entrada não pode ser vazia")
        }
    } while (entrada.isEmpty())

    return entrada
}

fun exibirInterface(tela: String) {
    when(tela) {
        "principal" -> {
            println("===================================")
            println("|          MENU PRINCIPAL          |")
            println("===================================")
            println("| 1 -> Cadastrar item               |")
            println("| 2 -> Atualizar item               |")
            println("| 3 -> Criar pedido                 |")
            println("| 4 -> Atualizar pedido             |")
            println("| 5 -> Consultar pedidos            |")
            println("| 6 -> Sair                         |")
            println("===================================")
        }
        "pedido" -> {
            println("===================================")
            println("| 1 | Adicionar itens ao pedido    |")
            println("| 2 | Finalizar pedido             |")
            println("| 3 | Limpar carrinho              |")
            println("| 4 | Voltar                       |")
            println("===================================")
        }
    }
}

fun gerenciarCadastroItem() {
    println("===================================")
    println("|         CADASTRO DE ITEM        |")
    println("===================================")

    val nomeItem = lerString("Nome do item: ")

    val descricaoItem = lerString("Descrição do item: ")

    val precoItem: BigDecimal = lerBigDecimal("Preço do item: ")

    val estoqueItem: Int = lerInt("Quantidade em estoque: ")

    adicionarItemAoCardapio(nomeItem, descricaoItem, precoItem, estoqueItem)
    println("Item adicionado com sucesso!!")
}

fun gerenciarAtualizacaoItem() {
    if(cardapio.isEmpty()) {
        println("Não há itens no cardápio\n")
    } else {
        println("===================================")
        println("|      ATUALIZAÇÃO DE ITENS       |")
        println("===================================")

        println("Qual item deseja atualizar?\n")

        cardapio.forEach { item -> println("${item.codigo} - ${item.nome}")}

        val codigoItemSelecionado: Int = lerInt("")

        val itemSelecionado: ItemCardapio? = buscarItemPorCodigo(codigoItemSelecionado)

        if(itemSelecionado == null) {
            println("Item não encontrado")
        } else {
            println("Deseja atualizar nome? Se sim, digite o novo nome, se não aperte Enter")
            val novoNome: String = readln()
            println("Deseja atualizar a descrição? Se sim, digite o novo nome, se não aperte Enter")
            val novaDescricao: String = readln()

            println("Deseja atualizar o preço? Se sim, digite o novo preço, se não aperte Enter")
            val novoPreco: BigDecimal? = readln().toBigDecimalOrNull()
            println("Deseja atualizar o estoque? Se sim, digite a quantidade a ser somada no estoque, se não aperte Enter")
            val adicaoEstoque: Int? = readln().toIntOrNull()

            atualizarItem(item = itemSelecionado, novoNome = novoNome, novaDescricao = novaDescricao, novoPreco = novoPreco, adicaoEstoque = adicaoEstoque)

            println("Produto atualizado: $itemSelecionado")
        }
    }
}

fun gerenciarCriacaoPedido() {
    if(cardapio.isEmpty()){
        println("\nNenhum item cadastrado no cardápio\n")
    } else {
        println("===================================")
        println("|       CRIAÇÃO DE PEDIDOS        |")
        println("===================================")

        var fazendoPedido = true

        val novoPedido = criarNovoPedido(codigoPedido)

        do {
            exibirInterface("pedido")

            val opcaoEscolhida: Int = lerInt("")

            when (opcaoEscolhida) {
                1 -> {adicionarItemAoPedido(novoPedido)}
                2 -> {
                    val pedidoFoiFinalizado = finalizarPedido(novoPedido)
                    if (pedidoFoiFinalizado) {
                        fazendoPedido = false
                    }
                }
                3 -> {
                    if (verificarSeCarrinhoEstaVazio(novoPedido)) {
                        println("O carrinho já está vazio")
                    } else {
                        limparCarrinho(novoPedido)

                        println("Carrinho limpo com sucesso!\n")
                    }
                }
                4 -> {
                    if (verificarSeCarrinhoEstaVazio(novoPedido)) {
                        fazendoPedido = false
                    } else {
                        println("Você deve finalizar o seu pedido ou limpar o seu carrinho!!!")
                    }
                }
                else -> println("Digite uma opção válida")
            }
        } while (fazendoPedido)
    }
}

fun adicionarItemAoPedido(pedido: Pedido) {
    var escolhendoItens = true

    do {
        println("Qual item deseja adicionar ao pedido?\n")
        cardapio.forEach { item -> println(
            "${item.codigo} - ${item.nome} | Descrição: ${item.descricao} | " +
                    "Preço: R$${item.preco} | Quantidade disponível: ${item.estoque}")}

        val codigoItemEscolhido: Int = lerInt("")

        val produtoEscolhido: ItemCardapio? = buscarItemPorCodigo(codigoItemEscolhido)

        if (produtoEscolhido == null) {
            println("Produto nao encontrado")
        } else {
            println("Item escolhido: ${produtoEscolhido.nome}")
            val quantidade = lerInt("Deseja adicionar quantos? (estoque disponível - ${produtoEscolhido.estoque}): ")

            if (quantidade <= 0 || quantidade > produtoEscolhido.estoque) {
                println("Digite a quantidade entre 1 e ${produtoEscolhido.estoque}")
            } else {

                val itemJaFoiAdicionado: PedidoItem? = itensPedido.find { pedidoItem -> pedidoItem.codigoPedido == codigoPedido && pedidoItem.codigoItem == produtoEscolhido.codigo }

                if(itemJaFoiAdicionado == null) {
                    itensPedido.add(PedidoItem(pedido.codigo, produtoEscolhido.codigo, quantidade))
                } else {
                    itemJaFoiAdicionado.quantidade = quantidade
                }

                println("${produtoEscolhido.nome} adicionado com sucesso\n")
            }

            escolhendoItens = false
        }
    } while (escolhendoItens)
}

fun finalizarPedido(pedido: Pedido): Boolean {
    val pedidoTemItem: Boolean = verificarSeCarrinhoEstaVazio(pedido)

    if (pedidoTemItem) {
        println("Você deve escolher no mínimo um item para finalizar o pedido")
        return false
    } else {
        println("Deseja adicionar cupom? Se sim, digite o cupom, se não aperte Enter")

        val porcentagemDesconto = BigDecimal("0.1")
        val cuponsValidos: List<String> = listOf("DEZ", "SEXTOU", "TIALU")

        var cupomValidoInserido = false
        var loopCupom = true

        do {
            val cupomDigitado: String = readln().trim().uppercase()

            if (cupomDigitado.isBlank()) {
                loopCupom = false
            } else if (cuponsValidos.contains(cupomDigitado)) {
                cupomValidoInserido = true
                loopCupom = false
            } else {
                println("Cupom inválido, tente novamente ou aperte Enter para prosseguir sem cupom")
            }
        } while (loopCupom)

        removerEstoqueProduto(pedido)

        if (cupomValidoInserido) {
            println("Aplicando cupom de 10% de desconto!")
            val valorComDesconto = pedido.valor * (BigDecimal.ONE - porcentagemDesconto)
            pedido.valor = valorComDesconto
        }

        pedido.pagamento = "Pago"
        pedidos.add(pedido)

        println("Pedido efetuado com sucesso. O total da sua conta é R$${pedido.valor}\n\n")

        codigoPedido++
        return true
    }
}

fun gerenciarAtualizacaoStatusPedido() {
    println("===========================================")
    println("|   ATUALIZAÇÃO DO STATUS DO PEDIDO       |")
    println("===========================================")

    if(pedidos.isEmpty()) {
        println("Sem pedidos por aqui\n")
    } else {
        println("Escolha o pedido para atualizar o status\n")
        println("PEDIDOS:")

        pedidos.forEach { pedido -> println("Número do pedido: ${pedido.codigo} - ${pedido.status} - total: R$${pedido.valor}")  }

        val codigoPedidoEscolhido: Int = lerInt("")

        val pedidoEscolhido: Pedido? = pedidos.find {
            it.codigo == codigoPedidoEscolhido
        }

        if (pedidoEscolhido == null) {
            println("Pedido não encontrado")
        } else {
            println("Deseja atualizar o pedido para qual Status? Status atual " +
                    "do pedido ${pedidoEscolhido.codigo}: ${pedidoEscolhido.status}\n")

            StatusPedido.entries.forEachIndexed {
                    index, status -> println("${index + 1} - $status")
            }

            val statusEscolhido: Int = lerInt("")

            if(statusEscolhido > StatusPedido.entries.size || statusEscolhido == 0){
                println("Status inexistente, selecione um status válido")
            } else {
                atualizarStatusPedido(pedidoEscolhido, statusEscolhido)

                println("Status do pedido ${pedidoEscolhido.codigo} alterado para ${pedidoEscolhido.status} " +
                        "com sucesso\n")
            }
        }
    }
}

fun gerenciarConsultaPedidos() {
    println("===================================")
    println("|       CONSULTA DE PEDIDOS       |")
    println("===================================")

    var estaConsultando = true

    do {
        if(pedidos.isEmpty()) {
            println("Sem pedidos por enquanto!")
            estaConsultando = false
        } else {
            val opcaoSair: Int = StatusPedido.entries.size + 2

            println("Vizualizar pedidos:")
            println("1 - VER TODOS")
            StatusPedido.entries.forEachIndexed { index, status -> println("${index + 2} - $status") }
            println("$opcaoSair - Sair")

            val opcaoEscolhida: Int = lerInt("")

            if (opcaoEscolhida == opcaoSair) {
                println("Saindo da consulta...")
                estaConsultando = false
            } else {
                val pedidosPorStatus: List<Pedido> = when (opcaoEscolhida) {
                    1 -> pedidos
                    in 2..StatusPedido.entries.size + 1 -> {
                        val statusEscolhido = StatusPedido.entries[opcaoEscolhida - 2]
                        pedidos.filter { it.status == statusEscolhido }
                    }
                    else -> {
                        emptyList()
                    }
                }

                if (pedidosPorStatus.isEmpty()) {
                    println("Nenhum pedido encontrado com esse status")
                } else {
                    pedidosPorStatus.forEach { pedido ->
                        println("\nPedido ${pedido.codigo} - VALOR: " +
                                "R$${pedido.valor} - ${pedido.pagamento} - ${pedido.status}")

                        val itensPedido: List<PedidoItem> = itensPedido.filter { it.codigoPedido == pedido.codigo }

                        print("Itens: ")
                        itensPedido.forEach { item ->
                            val itemCardapio = buscarItemPorCodigo(item.codigoItem)
                            print("| ${itemCardapio!!.nome}, quantidade: ${item.quantidade} | ") }
                    }
                    println("\n")
                }
            }
        }
    } while (estaConsultando)
}

fun main() {
    var estaNaInterface = true

    do {
        exibirInterface("principal")

        val opcaoEscolhida: Int = lerInt("Escolha uma opção: ")

        when(opcaoEscolhida) {
            1 -> {gerenciarCadastroItem()}
            2 -> {gerenciarAtualizacaoItem()}
            3 -> {gerenciarCriacaoPedido()}
            4 -> {gerenciarAtualizacaoStatusPedido()}
            5 -> {gerenciarConsultaPedidos()}
            6 -> estaNaInterface = false
            else -> println("Opção inválida: insira um número entre as opções exibidas no menu.")
        }
    } while (estaNaInterface)
}