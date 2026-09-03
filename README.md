1. Por que o ViewModel sozinho (etapas 2 e 3) não é suficiente para sobreviver à morte do
processo, mesmo sobrevivendo à rotação de tela?

Porque ele é um objeto na RAM, dentro do ViewModelStore da Activity.

2. Qual a diferença prática entre usar mutableStateOf e StateFlow dentro do ViewModel
nesta aplicação? Em algum momento essa diferença foi perceptível nos testes?

 A diferença na pratica é que mutableStateOf é do Compose (recomposição automática) ja o StateFlow é do coroutines que precisa de collectAsState(), mas é testável sem Compose, tem os operadores de Flow e serve em qualquer camada. 
 Nenhuma diferença pode ser perceptivel nos testes.

3. Se este placar precisasse ser salvo permanentemente (mesmo após o usuário fechar o
app e abrir dias depois), qual das quatro abordagens ainda seria insuficiente, e o que
seria necessário adicionar?

As quatro não teriam esse resultado esperado, qualquer uma delas, para termos esse salvamento seria necessario o uso de disco em si, para que pudesse ser possivel manter os dados ou gerar uma base de resultados.

4. Na sua opinião, qual abordagem você usaria em produção para este placar e por quê?

Em produção o ideal seria montar um combo de StateFlow + SavedStateHandle juntos. StateFlow pela arquitetura testável ja SavedStateHandle porque o cenário é real, caso o usuario feche em um placar alto tudo seria perdido.
