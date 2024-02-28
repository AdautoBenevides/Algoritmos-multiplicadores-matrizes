#!/bin/bash

# Função para executar o código Java com diferentes tamanhos de matriz
run_java_code() {
    order=$1
    echo "Running for order $order..."
    
    for i in {1..10}; do
        echo -n "Run $i: "
        java ConcurrentMatrixMultiplication $order
    done
}

# Executa o código Java para tamanhos pequenos
run_java_code 16
run_java_code 32
run_java_code 64

# Executa o código Java para tamanhos médios
#run_java_code 128
#run_java_code 256
#run_java_code 512

# Executa o código Java para tamanhos grandes
#run_java_code 1024
#run_java_code 2048
#run_java_code 4096
