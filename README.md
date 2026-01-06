## TCC-ORBITAE
## DUPLA: Gabriel Brenzinger & Nicolas Kaenana Silveira

>![image](https://github.com/user-attachments/assets/8878e888-730c-41a0-a30a-1b965f04cc18)

### INTRODUÇÃO:
> O ensino de física no Brasil utiliza de demonstrações práticas vísiveis muitas vezes com simulações,
> estas simulações geralmente não apresentam qualidade gráfica ou não condizem muito com a realidade.

### SOLUÇÃO:
> Para resolvermos isto, nosso trabalho contará não só com o software pronto, mas com o passo a passo
> para desenvolve-lo, assim outros professores e entusiastas podem replicá-lo, inovar ou mudar, contribuindo
> ainda mais para o ensino de física.

### ESCOPO:
>O ensino de cinemática no ensino médio tem se mostrado difícil, a dificuldade em transmitir conceitos como gravidade e colisão se deve a complexidade de visualizar estas idéias, as ferramentas atuais que se comprometem a resolver tal problema é simples e oferece pouca praticidade.
> Nosso projeto leva em conta isto, e buscará resolver projetando um software que simula a colisão e a gravidade, haverá gráficos objetivos a cada corpo do sistema.
> É de interesse diverso, tanto na educação quanto para usuários comuns, se espera que o trabalho possa entregar um software utilizável e que apresente as seguintes funcionalidades:
> - Gráficos a cada corpo selecionado.
> - Colisão condizente com a física newtoniana.
> - Gravidade condizente com a física newtoniana.
> - Vetores e indicadores de trajetória.
> - Entrega da documentação e relatórios.
>   
> O trabalho será demorado devido ao nível de complexidade, se espera que um protótipo funcional seja feito em 5 meses, não será feito esforço para o realismo gráfico e nem para a gamificação.
> As limitações do projeto será a própria linguagem que está sendo desenvolvido a aplicação; Java apresenta poucas ferramentas para manipulação 3D, e o desenvolvimento requer diversas bibliotecas.
> As limitações do Java leva a crença de possíveis riscos, sendo eles a impossibilidade de aplicar certos requisitos na aplicação, ou a dificuldade de resolvê-los.

### COMO EXECUTAR:
> Ao fazer o donwload do projeto, localize o TCC-Orbitae.jar.
> Passe o para uma pasta que contenha outra pasta chamada config.
> Na pasta config crie dois arquivos de texto, login e volume.
> Abra a pasta no cmd.
> Execute o seguinte comando no terminal:
>  ```
>  java --module-path endereco\da\biblioteca\javafx\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.fxml -jar TCC-Orbitae.jar
>  ```
>  O software tem uma API RESTful para salvar o cookie retornado do login, [site](https://github.com/NicolasKaenan/TCC-Orbitae-Site), então para testar primeiro envie essa requisição HTTP.
> ```
>  http://localhost:8080/auth/retorno-login?nomealeatorio
> ```
> Após isto é só testar :)
