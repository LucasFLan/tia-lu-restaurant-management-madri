import java.math.BigDecimal


fun main() {

    val pedidos = mutableListOf<Pedido>()

    var estaNaInterface: Boolean = true

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
                val nomeItem = readln()

                println("Descrição do item :")
                val descricaoItem = readln()

                try {
                    println("Preço")
                    val precoItem: BigDecimal = readln().toBigDecimal()

                    println("Quantidade em estoque")
                    val estoqueItem: Int = readln().toInt()

                    adicionar_item_ao_cardapio(nomeItem, descricaoItem, precoItem, estoqueItem)
                    println("Item adicionado com sucesso!!")

                } catch (e: NumberFormatException){
                    // Mudar essa mesnsagem de erro //
                    println("Entrada inválida, digite um número para preço e estoque")
                }
            }
            2 -> {
                println("===================================")
                println("|      ATUALIZAÇÃO DE ITENS       |")
                println("===================================")
                if(cardapio.isEmpty()) {
                    println("Sem itens no menu\n")
                } else {
                    println("Qual item deseja atualizar?\n")

                    cardapio.forEach { item -> "${item.codigo} - ${item.codigo}"}

                    val codigoItemSelecionado: Int = readln().toInt()

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

                            if(novoNome != "") {
                                itemSelecionado.nome = novoNome
                            }

                            if(novaDescricao != "") {
                                itemSelecionado.descricao = novaDescricao
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
                println("===================================")
                println("|       CRIAÇÃO DE PEDIDOS        |")
                println("===================================")

                var fazendoPedido = true
                var escolhendoItens = true
                val itensEscolhidos = mutableListOf<ItemCardapio>()
                var quantidade: Int = 0


                val numeroPedido = when {
                    pedidos.isEmpty() -> 0
                    else -> pedidos.lastIndex + 1
                }
                // Jogar a criação do pedido para a ultima coisa do processo
                val novoPedido = Pedido(numeroPedido = numeroPedido, itens = itensEscolhidos, pagamento = "Em análise",
                    valor = BigDecimal(0.0), status = Status.ACEITO)

                do {
                    println("===================================")
                    println("| 1 | Adicionar itens ao pedido    |")
                    println("| 2 | Finalizar pedido             |")
                    println("| 3 | Limpar carrinho              |")
                    println("| 4 | Voltar                       |")
                    println("===================================")

                    val opcaoEscolhida: Int? = readln().toIntOrNull()

                    when (opcaoEscolhida) {
                        1 -> {
                            do {
                                if (cardapio.isEmpty()) {
                                    println("Nenhum item cadastrado\n")
                                } else {

                                println("Qual item deseja adicionar ao pedido?\n")
                                for (item in cardapio) {
                                    println(
                                        "${item.codigo} - ${item.nome} | Descrição: ${item.descricao} | " +
                                                "Preço: R$${item.preco} | Quantidade disponível: ${item.estoque}"
                                    )
                                }

                                val codigoItemEscolhido: Int? = readln().toIntOrNull()

                                val produtoEscolhido: ItemCardapio? = cardapio.find { it.codigo == codigoItemEscolhido }

                                if (produtoEscolhido == null) {
                                    println("Produto nao encontrado")
                                } else {

                                    println("Item escolhido: ${produtoEscolhido.nome}")
                                    println("Deseja adicionar quantos? (estoque disponível: ${produtoEscolhido.estoque})")
                                    quantidade = readln().toInt()

                                    // Mudar a mensagem pra ("Digite a quantidade entre 1 e ${quantidade.estoque}")
                                    if (quantidade <= 0 || quantidade > produtoEscolhido.estoque) {
                                        println("Número no estoque indisponível")
                                    } else {
                                        itensEscolhidos.add(produtoEscolhido)
                                        produtoEscolhido.estoque = produtoEscolhido.estoque - quantidade

                                        novoPedido.valor += (produtoEscolhido.preco * quantidade.toBigDecimal())

                                        println("${produtoEscolhido.nome} adicionado com sucesso\n")
                                    }

                                    escolhendoItens = false
                                }
                            }
                            } while (escolhendoItens)
                        }
                        2 -> {
                            if(novoPedido.itens.isEmpty()) {
                                println("Você deve escolher no minímo um item para finalizar o pedido")

                            } else {
                                println("Total do seu pedido: ${novoPedido.valor}")
                                println("Deseja adicionar cupom? Se sim, digite cupom, se não aperte Enter")
                                val querCupom: String = readln()
                                val porcentagemDesconto: BigDecimal = BigDecimal(0.9)
                                var calculoCupom = (novoPedido.valor * porcentagemDesconto)

                                // arrumar esse bloco de código para fazer o calculo de porcetagem dps do user pedir o desconto especifico
                                if (querCupom.uppercase() == "SIM"){
                                    println("Digite o Cupom :")
                                    val cupom: String = readln()
                                    when(cupom.uppercase()) {
                                        "DEZ" -> println("O valor total é ${calculoCupom}, pagamento com cupom")
                                        else -> println("Cupom inválido")
                                    }
                                } else {
                                    println(novoPedido.valor)
                                }
                                pedidos.add(novoPedido)

                                println("Pedido efetuado com sucesso. O total da sua conta é $calculoCupom\n\n")

                                pedidos[numeroPedido].pagamento = "Pago"
                                fazendoPedido = false
                                    }
                                }
                        3 -> {
                            if (itensEscolhidos.isEmpty()) {
                                println("O carrinho está vazio")
                            } else {
                                itensEscolhidos.forEach {
                                    cardapio[it.codigo - 1].estoque += quantidade
                                }
                                novoPedido.valor = BigDecimal(0.0)
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
                        else -> println("Digite uma opção válida")
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

                    pedidos.forEach { pedido -> println("Número do pedido: ${pedido.numeroPedido} - ${pedido.status}\n - total: R$${pedido.valor}")  }

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
                            1 -> pedidos[numeroPedido].status = Status.FAZENDO
                            2 -> pedidos[numeroPedido].status = Status.FEITO
                            3 -> pedidos[numeroPedido].status = Status.ESPERANDO_ENTREGADOR
                            4 -> pedidos[numeroPedido].status = Status.SAIU_PARA_ENTREGA
                            5 -> pedidos[numeroPedido].status = Status.ENTREGUE
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
                    for ((index, status) in Status.entries.withIndex()) {
                        println("${index + 2} - $status")
                    }
                    println("${Status.entries.size + 2} - Sair")

                    val opcaoEscolhida: Int = readln().toInt()
                    var pedidosPorStatus: List<Pedido>

                    when (opcaoEscolhida) {
                        1 -> pedidosPorStatus = pedidos
                        in 2..Status.entries.size + 1 -> {
                            val statusEscolhido = Status.entries[opcaoEscolhida - 2]
                            pedidosPorStatus = pedidos.filter { it.status == statusEscolhido }
                        }
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
            6 -> estaNaInterface = false
            else -> println("Opção inválida")
        }
    } while (estaNaInterface)
}