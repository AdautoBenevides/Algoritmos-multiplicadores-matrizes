#!/bin/bash

# Compilar os arquivos Java
javac TCPServer.java
javac TCPClient.java

# Número de execuções
num_execucoes=1

# Ordem da matriz


# Executar o sistema distribuído várias vezes
for ((i=1; i<=$num_execucoes; i++)); do
    # Iniciar o servidor
    # echo "Iniciando o servidor..."
    java TCPServer.java 8192 &

    # Esperar um segundo para garantir que o servidor esteja funcionando
    sleep 1

    # Iniciar os clientes
    # echo "Iniciando os clientes..."
    java TCPClient.java &
    java TCPClient.java &
    java TCPClient.java &
    java TCPClient.java

    # Esperar um pouco para dar tempo para o cliente se conectar e enviar dados
    sleep 1

    # Aguardar o servidor e os clientes terminarem
    wait

    # Aguardar um tempo antes da próxima execução
    # echo "Execução $i concluída."
    sleep 1
done

# Remover os arquivos .class
rm *.class

echo "Todas as execuções concluídas."
