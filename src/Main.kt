fun main() {

    var pedidos = mutableListOf<Pedido>()

    var isOnInterface: Boolean = true

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
        print("Escolha uma opção: ")

        val opcaoEscolhida: Int = readln().toInt()

        when(opcaoEscolhida) {
            1 -> {
                println("===================================")
                println("|         CADASTRO DE ITEM        |")
                println("===================================")
                println("Nome do item")
                val nomeItem: String = readln()

                println("Descrição do item")
                val descricaoItem: String = readln()

                try {
                    println("Preço")
                    val precoItem: Float = readln().toFloat()

                    println("Quantidade em estoque")
                    val estoqueItem: Int = readln().toInt()

                    adicionarItemAoMenu(nomeItem, descricaoItem, precoItem, estoqueItem)
                    println("Item adicionado com sucesso!!")

                } catch (e: NumberFormatException){
                    println("Entrada inválida, digite um número para preço e estoque")
                }
            }
            2 -> {
                println("===================================")
                println("|      ATUALIZAÇÃO DE ITENS       |")
                println("===================================")
                if(menu.isEmpty()) {
                    println("Sem itens no menu\n")
                } else {
                    println("Qual item deseja atualizar?\n")

                    for (item in menu) {
                        println("${item.codigo} - ${item.nome}")
                    }

                    val codigoItemSelecionado: Int = readln().toInt()

                    val itemSelecionado: Produto? = menu.find { it.codigo == codigoItemSelecionado }

                    if(itemSelecionado == null) {
                        println("Item não encontrado")
                    } else {
                        println("Deseja atualizar nome? Se sim, digite o novo nome, se não aperte Enter")
                        val novoNome: String = readln()
                        println("Deseja atualizar a descrição? Se sim, digite o novo nome, se não aperte Enter")
                        val novaDescricao: String = readln()

                        try {
                            println("Deseja atualizar o preço? Se sim, digite o novo preço, se não digite 0")
                            val novoPreco: Float = readln().toFloat()
                            println("Deseja atualizar o estoque? Se sim, digite a quantidade a ser somada no estoque, se não digite 0")
                            val novoEstoque: Int = readln().toInt()

                            if(novoNome != "") {
                                itemSelecionado.nome = novoNome
                            }

                            if(novaDescricao != "") {
                                itemSelecionado.descricao = novaDescricao
                            }

                            if(novoPreco != 0f){
                                itemSelecionado.preco = novoPreco
                            }

                            if(novoEstoque != 0) {
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
                println("===================================")
                println("|       CRIAÇÃO DE PEDIDOS        |")
                println("===================================")

                var fazendoPedido = true
                var escolhendoItens = true
                var itensEscolhidos = mutableListOf<Produto>()
                var quantidade: Int = 0


                val numeroPedido = when {
                    pedidos.isEmpty() -> 0
                    else -> pedidos.lastIndex + 1
                }

                var novoPedido = Pedido(numeroPedido = numeroPedido, itens = itensEscolhidos, pagamento = "Em análise",
                    valor = 0f, status = OrderStatus.ACEITO)

                do {
                    println("===================================")
                    println("| 1 | Adicionar itens ao pedido    |")
                    println("| 2 | Finalizar pedido             |")
                    println("| 3 | Limpar carrinho              |")
                    println("| 4 | Voltar                       |")
                    println("===================================")

                    val opcaoEscolhida: Int = readln().toInt()

                    when (opcaoEscolhida) {
                        1 -> {
                            do {
                                if(menu.isEmpty()) {
                                    println("Nenhum item cadastrado\n")
                                    break
                                }

                                println("Qual item deseja adicionar ao pedido?\n")
                                for (item in menu) {
                                    println("${item.codigo} - ${item.nome} | Descrição: ${item.descricao} | " +
                                            "Preço: R$${item.preco} | Quantidade disponível: ${item.estoque}")
                                }

                                val codigoItemEscolhido: Int = readln().toInt()

                                val produtoEscolhido: Produto? = menu.find {it.codigo == codigoItemEscolhido}

                                if(produtoEscolhido == null){
                                    println("Produto nao encontrado")
                                    break
                                }

                                println("Item escolhido: ${produtoEscolhido.nome}")
                                println("Deseja adicionar quantos? (estoque disponível: ${produtoEscolhido.estoque})")
                                quantidade = readln().toInt()

                                if(produtoEscolhido.estoque < quantidade || quantidade == 0) {
                                    println("Número no estoque indisponível")
                                    break
                                } else {
                                    itensEscolhidos.add(produtoEscolhido)
                                    produtoEscolhido.estoque = produtoEscolhido.estoque - quantidade

                                    novoPedido.valor += (produtoEscolhido.preco * quantidade)

                                    println("${produtoEscolhido.nome} adicionado com sucesso\n")
                                }

                                escolhendoItens = false
                            } while (escolhendoItens)
                        }
                        2 -> {
                            var total: Float = 0f
                            if(novoPedido.itens.isEmpty()) {
                                println("Você deve escolher no minímo um item para finalizar o pedido")

                            } else {
                                println("Total do seu pedido: ${novoPedido.valor}")
                                println("Deseja adicionar cupom? Se sim, digite cupom, se não aperte Enter")
                                val cupom: String = readln()

                                val porcentagemDesconto: Float = 0.1f

                                when {
                                    cupom == "DEZ" -> total = novoPedido.valor * (1 - porcentagemDesconto)
                                    cupom == "" -> {
                                        println("Pagamento sem cupom")
                                        total = novoPedido.valor
                                    }
                                    else -> {
                                        println("Cupom inválido")
                                        total = novoPedido.valor
                                    }
                                }

                                pedidos.add(novoPedido)

                                println("Pedido efetuado com sucesso. O total da sua conta é $total\n\n")

                                pedidos[numeroPedido].pagamento = "Pago"
                                fazendoPedido = false
                            }
                        }
                        3 -> {
                            if (itensEscolhidos.isEmpty()) {
                                println("O carrinho está vazio")
                            } else {
                                itensEscolhidos.forEach {
                                    menu[it.codigo - 1].estoque += quantidade
                                }
                                novoPedido.valor = 0f
                                itensEscolhidos.clear()
                                println("Carrinho limpo com sucesso!\n")
                            }
                        }
                        4 -> {
                            if (itensEscolhidos.isEmpty()) {
                                fazendoPedido = false
                            } else {
                                println("Você deve finalizar o seu pedido ou limpar o seu carrinho!!!")
                            }
                        }
                    }
                } while (fazendoPedido)
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
                    for (pedido in pedidos) {
                        println("Número do pedido: ${pedido.numeroPedido} - ${pedido.status}\n - total: R$${pedido.valor}")
                    }

                    val codigoPedidoEscolhido: Int = readln().toInt()

                    val pedidoEscolhido: Pedido? = pedidos.find {
                        it.numeroPedido == codigoPedidoEscolhido
                    }

                    if (pedidoEscolhido == null) {
                        println("Pedido não encontrado")
                    } else {
                        println("Deseja atualizar o pedido para qual Status? Status atual " +
                                "do pedido ${pedidoEscolhido.numeroPedido}: ${pedidoEscolhido.status}\n")

                        println("1 - FAZENDO")
                        println("2 - FEITO")
                        println("3 - ESPERANDO ENTREGADOR")
                        println("4 - SAIU PARA ENTREGA")
                        println("5 - ENTREGUE")

                        val statusEscolhido: Int = readln().toInt()

                        val numeroPedido = pedidoEscolhido.numeroPedido

                        when(statusEscolhido) {
                            1 -> pedidos[numeroPedido].status = OrderStatus.FAZENDO
                            2 -> pedidos[numeroPedido].status = OrderStatus.FEITO
                            3 -> pedidos[numeroPedido].status = OrderStatus.ESPERANDO_ENTREGADOR
                            4 -> pedidos[numeroPedido].status = OrderStatus.SAIU_PARA_ENTREGA
                            5 -> pedidos[numeroPedido].status = OrderStatus.ENTREGUE
                        }

                        println("Status do pedido ${pedidoEscolhido.numeroPedido} alterado para ${pedidoEscolhido.status} " +
                                "com sucesso\n")
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
                        println("Sem pedidos por enquanto!\n")
                        break
                    }

                    println("Vizualizar pedidos:")
                    println("1 - VER TODOS")
                    println("2 - ACEITO")
                    println("3 - FAZENDO")
                    println("4 - FEITO")
                    println("5 - ESPERANDO ENTREGADOR")
                    println("6 - SAIU PARA ENTREGA")
                    println("7 - ENTREGUE")
                    println("8 - Sair da consulta")

                    val opcaoEscolhida: Int = readln().toInt()
                    var pedidosPorStatus: List<Pedido> = listOf()

                    when (opcaoEscolhida) {
                        1 -> {
                            for (pedido in pedidos) {
                                println("Pedido ${pedido.numeroPedido} - Itens: ${pedido.itens} - VALOR: R$${pedido.valor} - ${pedido.pagamento} - ${pedido.status}")
                            }
                        }
                        2 -> pedidosPorStatus = pedidos.filter { it.status == OrderStatus.ACEITO }
                        3 -> pedidosPorStatus = pedidos.filter { it.status == OrderStatus.FAZENDO }
                        4 -> pedidosPorStatus = pedidos.filter { it.status == OrderStatus.FEITO }
                        5 -> pedidosPorStatus = pedidos.filter { it.status == OrderStatus.ESPERANDO_ENTREGADOR }
                        6 -> pedidosPorStatus = pedidos.filter { it.status == OrderStatus.SAIU_PARA_ENTREGA }
                        7 -> pedidosPorStatus = pedidos.filter { it.status == OrderStatus.ENTREGUE }
                        8 -> {
                            println("Saindo...")
                            estaConsultando = false
                            break
                        }
                        else -> {
                            println("Opção inválida!")
                            break
                        }
                    }

                    if (pedidosPorStatus.isEmpty() && opcaoEscolhida != 1) {
                        println("Nenhum pedido encontrado com esse status")
                    } else {
                        for (pedido in pedidosPorStatus) {
                            println("Pedido ${pedido.numeroPedido} - Itens: ${pedido.itens} - VALOR: R$${pedido.valor} - ${pedido.pagamento} - ${pedido.status}")
                        }
                    }

                } while (estaConsultando)
            }
            6 -> isOnInterface = false
            else -> println("Opção inválida")
        }
    } while (isOnInterface)
}