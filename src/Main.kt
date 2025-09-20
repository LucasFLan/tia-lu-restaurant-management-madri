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

fun <T> lerEntradaValida(mensagem: String, valorAtual: T, conversor: (String) -> T): T {
    var resultado: T? = null

    do {
        print("$mensagem (Atual: $valorAtual)")
        val entrada = readln()

        if (entrada.trim() == ""){
            resultado = valorAtual
        }

        try {
            resultado = conversor(entrada)
        } catch (e: Exception) {
            println("Entrada inválida. Por favor, tente novamente.")
        }
    } while (resultado == null)

    return resultado
}


fun main() {

    var estaNaInterface = true

    do {
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

        val opcaoEscolhida: Int = lerInt("Escolha uma opção: ")

        when(opcaoEscolhida) {
            1 -> {

                println("===================================")
                println("|         CADASTRO DE ITEM        |")
                println("===================================")

                val nomeItem = lerString("Nome do item: ")

                val descricaoItem = lerString("Descrição do item: ")

                val precoItem: BigDecimal = lerBigDecimal("Preço do item: ")

                val estoqueItem: Int = lerInt("Quantidade em estoque: ")

                cadastrarItemAoCardapio(nomeItem, descricaoItem, precoItem, estoqueItem)
                println("Item adicionado com sucesso!!")
            }
            2 -> {
                println("===================================")
                println("|      ATUALIZAÇÃO DE ITENS       |")
                println("===================================")

                if(cardapio.isEmpty()) {
                    println("Não há itens no cardápio\n")
                } else {
                    println("Qual item deseja atualizar?\n")

                    cardapio.forEach { item -> println("${item.codigo} - ${item.nome}")}

                    val codigoItemSelecionado: Int = lerInt("")

                    val itemSelecionado: ItemCardapio? = cardapio.find { it.codigo == codigoItemSelecionado }

                    if(itemSelecionado == null) {
                        println("Item não encontrado")
                    } else {
                        println("Deseja atualizar nome? Se sim, digite o novo nome, se não aperte Enter")
                        val novoNome: String = readln()
                        println("Deseja atualizar a descrição? Se sim, digite o novo nome, se não aperte Enter")
                        val novaDescricao: String = readln()

                        try {
                            println("Deseja atualizar o preço? Se sim, digite o novo preço, se não aperte Enter")
                            val novoPreco: BigDecimal? = readln().toBigDecimalOrNull()
                            println("Deseja atualizar o estoque? Se sim, digite a quantidade a ser somada no estoque, se não aperte Enter")
                            val novoEstoque: Int? = readln().toIntOrNull()

                            if(novoNome.trim() != "") {
                                itemSelecionado.nome = novoNome.trim()
                            }

                            if(novaDescricao.trim() != "") {
                                itemSelecionado.descricao = novaDescricao.trim()
                            }

                            if(novoPreco != null){
                                itemSelecionado.preco = novoPreco
                            }

                            if(novoEstoque != null) {
                                itemSelecionado.estoque += novoEstoque
                            }

                            println("Produto atualizado: $itemSelecionado")
                        } catch (e: NumberFormatException) {
                            println("Entrada inválida, digite um número para preço e estoque")
                        }
                    }
                }
            }
            3 -> {
                if(cardapio.isEmpty()){
                    println("\nNenhum item cadastrado no cardápio\n")
                } else {
                    println("===================================")
                    println("|       CRIAÇÃO DE PEDIDOS        |")
                    println("===================================")

                    var fazendoPedido = true
                    var escolhendoItens = true
                    var quantidade = 0

                    val novoPedido = Pedido(numeroPedido = numeroPedido, pagamento = "Em análise",
                        valor = BigDecimal("0"), status = StatusPedido.ACEITO)

                    do {
                        println("===================================")
                        println("| 1 | Adicionar itens ao pedido    |")
                        println("| 2 | Finalizar pedido             |")
                        println("| 3 | Limpar carrinho              |")
                        println("| 4 | Voltar                       |")
                        println("===================================")

                        val opcaoEscolhida: Int = lerInt("")

                        when (opcaoEscolhida) {
                            1 -> {
                                do {
                                    println("Qual item deseja adicionar ao pedido?\n")
                                    for (item in cardapio) {
                                        println(
                                            "${item.codigo} - ${item.nome} | Descrição: ${item.descricao} | " +
                                                    "Preço: R$${item.preco} | Quantidade disponível: ${item.estoque}"
                                        )
                                    }

                                    val codigoItemEscolhido: Int = lerInt("")

                                    val produtoEscolhido: ItemCardapio? = cardapio.find { it.codigo == codigoItemEscolhido }

                                    if (produtoEscolhido == null) {
                                        println("Produto nao encontrado")
                                    } else {
                                        println("Item escolhido: ${produtoEscolhido.nome}")
                                        quantidade = lerInt("Deseja adicionar quantos? (estoque disponível - ${produtoEscolhido.estoque}): ")

                                        if (quantidade <= 0 || quantidade > produtoEscolhido.estoque) {
                                            println("Digite a quantidade entre 1 e ${produtoEscolhido.estoque}")
                                        } else {

                                            val itemJaFoiAdicionado: PedidoItem? = itensPedido.find { pedidoItem -> pedidoItem.codigoPedido == numeroPedido && pedidoItem.codigoItem == produtoEscolhido.codigo }

                                            if(itemJaFoiAdicionado == null) {
                                                itensPedido.add(PedidoItem(novoPedido.numeroPedido, produtoEscolhido.codigo, quantidade))
                                            } else {
                                                itemJaFoiAdicionado.quantidade = quantidade
                                            }

                                            novoPedido.valor += (produtoEscolhido.preco * quantidade.toBigDecimal())

                                            println("${produtoEscolhido.nome} adicionado com sucesso\n")
                                        }
                                        escolhendoItens = false
                                    }
                                } while (escolhendoItens)
                            }
                            2 -> {
                                val pedidoTemItem: Boolean = itensPedido.none {it.codigoPedido == novoPedido.numeroPedido}

                                if(pedidoTemItem) {
                                    println("Você deve escolher no minímo um item para finalizar o pedido")
                                } else {
                                    println("Total do seu pedido: ${novoPedido.valor}")
                                    println("Deseja adicionar cupom? Se sim, digite cupom, se não aperte Enter")

                                    val porcentagemDesconto = BigDecimal("0.1")
                                    val cuponsValidos: List<String> = listOf("DEZ", "SEXTOU", "TIALU")
                                    var cupomAutorizado = false

                                    do {
                                        val querCupom: String = readln()

                                        if (querCupom.trim() == ""){
                                            cupomAutorizado = true
                                        } else {
                                            if (cuponsValidos.contains(querCupom.trim().uppercase())) {
                                                val valorComDesconto = novoPedido.valor * (BigDecimal.ONE - porcentagemDesconto)
                                                novoPedido.valor = valorComDesconto
                                                cupomAutorizado = true
                                            } else {
                                                println("Cupom inválido, tente novamente ou aperte Enter para prosseguir sem cupom")
                                            }
                                        }
                                    } while (!cupomAutorizado)

                                    novoPedido.pagamento = "Pago"

                                    itensPedido.forEach { itemPedido ->
                                        if (itemPedido.codigoPedido == novoPedido.numeroPedido) {
                                            val itemEscolhido = cardapio.find { item -> itemPedido.codigoItem == item.codigo }

                                            itemEscolhido!!.estoque -= itemPedido.quantidade

                                        }
                                    }

                                    pedidos.add(novoPedido)

                                    println("Pedido efetuado com sucesso. O total da sua conta é R$${novoPedido.valor}\n\n")

                                    numeroPedido++

                                    println(itensPedido)

                                    fazendoPedido = false
                                }
                            }
                            3 -> {
                                if (itensPedido.isEmpty()) {
                                    println("O carrinho já está vazio")
                                } else {
                                    itensPedido.forEach {
                                        cardapio[it.codigoItem - 1].estoque += quantidade
                                    }
                                    novoPedido.valor = BigDecimal("0")
                                    itensPedido.clear()
                                    println("Carrinho limpo com sucesso!\n")
                                }
                            }
                            4 -> {
                                if (itensPedido.isEmpty()) {
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
            4 -> {
                println("===========================================")
                println("|   ATUALIZAÇÃO DO STATUS DO PEDIDO       |")
                println("===========================================")

                if(pedidos.isEmpty()) {
                    println("Sem pedidos por aqui\n")
                } else {
                    println("Escolha o pedido para atualizar o status\n")
                    println("PEDIDOS:")

                    pedidos.forEach { pedido -> println("Número do pedido: ${pedido.numeroPedido} - ${pedido.status} - total: R$${pedido.valor}")  }

                    val codigoPedidoEscolhido: Int = lerInt("")

                    val pedidoEscolhido: Pedido? = pedidos.find {
                        it.numeroPedido == codigoPedidoEscolhido
                    }

                    if (pedidoEscolhido == null) {
                        println("Pedido não encontrado")
                    } else {
                        println("Deseja atualizar o pedido para qual Status? Status atual " +
                                "do pedido ${pedidoEscolhido.numeroPedido}: ${pedidoEscolhido.status}\n")

                        StatusPedido.entries.forEachIndexed {
                            index, status -> println("${index + 1} - $status")
                        }

                        val statusEscolhido: Int = lerInt("")

                        if(statusEscolhido > StatusPedido.entries.size || statusEscolhido == 0){
                            println("Status inexistente, selecione um status válido")
                        } else {
                            pedidoEscolhido.status = StatusPedido.entries[statusEscolhido - 1]

                            println("Status do pedido ${pedidoEscolhido.numeroPedido} alterado para ${pedidoEscolhido.status} " +
                                    "com sucesso\n")
                        }
                    }
                }
            }
            5 -> {
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
                                    println("\nPedido ${pedido.numeroPedido} - VALOR: " +
                                        "R$${pedido.valor} - ${pedido.pagamento} - ${pedido.status}")

                                    val itensPedido: List<PedidoItem> = itensPedido.filter { it.codigoPedido == pedido.numeroPedido }

                                    print("Itens: ")
                                    itensPedido.forEach { item ->
                                        val nomeItem = cardapio.find { it.codigo == item.codigoItem }
                                        print("| ${nomeItem!!.nome}, quantidade: ${item.quantidade} | ") }
                                }
                                println("\n")
                            }
                        }
                    }
                } while (estaConsultando)
            }
            6 -> estaNaInterface = false
            else -> println("Opção inválida: insira um número entre as opções exibidas no menu.")
        }
    } while (estaNaInterface)
}