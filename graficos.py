import matplotlib.pyplot as plt
import numpy as np

# Dados para execução sequencial
orders_seq = [256, 512, 1024, 2048, 4096]
means_seq = [0.0312, 0.3263, 1.9522, 67.9995, 826.2073]
intervals_seq = [(0.02962549, 0.03277451),
                 (0.3058013, 0.3467987),
                 (1.881016, 2.023384),
                 (66.47555, 69.52345),
                 (804.9634, 847.4512)]

# Dados para execução concorrente (a partir de 256)
orders_conc = [256, 512, 1024, 2048, 4096]
means_conc = [0.031, 0.686, 1.9467, 36.225, 347.1921]
intervals_conc = [(0.02962549, 0.03277451),
                  (0.6554781, 0.7165219),
                  (1.836883, 2.056517),
                  (35.60933, 36.84067),
                  (340.0699, 354.3143)]

# Tamanhos pequenos (256 e 512)
orders_small = [256, 512]
means_small_seq = [means_seq[0], means_seq[1]]
means_small_conc = [means_conc[0], means_conc[1]]

# Tamanhos grandes (1024, 2048, 4096)
orders_large = [1024, 2048, 4096]
means_large_seq = means_seq[2:]
means_large_conc = means_conc[2:]

# Criar gráfico para tamanhos pequenos
fig, ax1 = plt.subplots(figsize=(8, 5))

bar_width = 0.35
bar_gap = 0.2
opacity = 0.8

# Corrigir o formato dos intervalos de erro
yerr_small_seq = np.array([(i[1] - m, m - i[0])
                          for m, i in zip(means_small_seq, intervals_seq[:2])]).T
yerr_small_conc = np.array([(i[1] - m, m - i[0])
                           for m, i in zip(means_small_conc, intervals_conc[:2])]).T

# Posições das barras
pos_small_seq = np.arange(len(orders_small))
pos_small_conc = pos_small_seq + bar_width + bar_gap

# Plotar barras sequenciais para tamanhos pequenos
bars_small_seq = ax1.bar(pos_small_seq, means_small_seq, bar_width,
                         alpha=opacity, yerr=yerr_small_seq, label='Sequencial', color='blue')

# Plotar barras concorrentes para tamanhos pequenos
bars_small_conc = ax1.bar(pos_small_conc, means_small_conc, bar_width,
                          alpha=opacity, yerr=yerr_small_conc, label='Concorrente', color='orange')

ax1.set_xlabel('Ordem')
ax1.set_ylabel('Tempo Médio (s)')
ax1.set_title(
    'Comparação de Execução Sequencial e Concorrente (Tamanhos Pequenos)')
ax1.set_xticks(pos_small_seq + (bar_width + bar_gap) / 2)
ax1.set_xticklabels(orders_small)
ax1.legend()

# Criar gráfico para tamanhos grandes
fig, ax2 = plt.subplots(figsize=(8, 5))

# Corrigir o formato dos intervalos de erro para tamanhos grandes
yerr_large_seq = np.array([(i[1] - m, m - i[0])
                          for m, i in zip(means_large_seq, intervals_seq[2:])]).T
yerr_large_conc = np.array([(i[1] - m, m - i[0])
                           for m, i in zip(means_large_conc, intervals_conc[2:])]).T

# Posições das barras para tamanhos grandes
pos_large_seq = np.arange(len(orders_large))
pos_large_conc = pos_large_seq + bar_width + bar_gap

# Plotar barras sequenciais para tamanhos grandes
bars_large_seq = ax2.bar(pos_large_seq, means_large_seq, bar_width,
                         alpha=opacity, yerr=yerr_large_seq, label='Sequencial', color='blue')

# Plotar barras concorrentes para tamanhos grandes
bars_large_conc = ax2.bar(pos_large_conc, means_large_conc, bar_width,
                          alpha=opacity, yerr=yerr_large_conc, label='Concorrente', color='orange')

ax2.set_xlabel('Ordem')
ax2.set_ylabel('Tempo Médio (s)')
ax2.set_title(
    'Comparação de Execução Sequencial e Concorrente (Tamanhos Grandes)')
ax2.set_xticks(pos_large_seq + (bar_width + bar_gap) / 2)
ax2.set_xticklabels(orders_large)
ax2.legend()

plt.tight_layout()
plt.show()
