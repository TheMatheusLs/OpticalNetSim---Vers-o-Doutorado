# Simulador de Redes Ópticas Elásticas - Simão

Este é um simulador de redes ópticas elásticas (EON) escrito em Java. Ele foi adaptado para melhorar a legibilidade e o desempenho em comparação com a versão original usada em um projeto de mestrado.

## Funcionalidades

O simulador permite a criação de redes ópticas elásticas personalizadas e a simulação de fluxos de tráfego por meio dessas redes. Algumas das funcionalidades incluem:
- Criar topologias de rede com diferentes nós e enlaces
- Definir demandas de tráfego com diferentes requisitos de largura de banda e qualidade de serviço
- Alocar espectro óptico para as demandas usando diferentes algoritmos RMLSA (Routing and Modulation Level and Spectrum Assignment)
- Avaliar o desempenho da rede em termos de taxa de bloqueio, utilização do espectro e consumo energético

## Requisitos de instalação

Para instalar o simulador, siga os seguintes passos:

- Clone ou baixe este repositório para a sua máquina local
- Importe o projeto no Eclipse IDE ou similar
- Execute a classe Main.java

### Uso
Para usar o simulador, você precisa configurar os parâmetros da rede e das demandas no arquivo SimulationConfig.txt. Os parâmetros são:

- Número de nós da rede
- Número total de fibras por enlace
- Número total de demandas geradas
- Taxa média das demandas em bits por segundo
- Tempo médio entre chegadas das demandas em segundos
- Tempo médio de duração das demandas em segundos
- Algoritmo RMLSA usado na alocação do espectro
- Após configurar os parâmetros, execute a classe Main.java e aguarde os resultados. O simulador irá gerar gráficos e arquivos com as métricas da rede.

