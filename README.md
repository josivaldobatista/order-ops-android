# 📱 OrderOps Android

Aplicação Android do sistema **OrderOps**, responsável pela interface mobile para gerenciamento de pedidos, mesas, produtos e pagamentos.

Este app consome a API do projeto:
👉 https://github.com/seu-usuario/order-ops-service

---

## 🚀 Tecnologias

- Kotlin
- Jetpack Compose
- Retrofit
- OkHttp
- Coroutines
- StateFlow
- Clean Architecture (adaptada)

---

## 🧱 Arquitetura

O projeto segue uma separação de responsabilidades inspirada em Clean Architecture:
presentation → UI (Compose + ViewModel)
domain → regras de negócio (UseCases)
data → API, DTOs e Repository
core → infraestrutura (network, storage, utils)


### Estrutura de pastas
```
com.jfb.orderops
├── core
│ ├── network
│ ├── storage
│ ├── result
│ └── ui
│
├── auth
│ ├── data
│ ├── domain
│ └── presentation
│
├── dashboard
│ └── presentation

```


---

## 🔐 Autenticação

O app utiliza autenticação via **JWT**.

Fluxo:

1. Usuário realiza login
2. API retorna `accessToken`
3. Token é salvo localmente (`SharedPreferences`)
4. Um `AuthInterceptor` adiciona automaticamente:



em todas as requisições

---

## 🌐 Configuração da API

Para rodar localmente em dispositivo físico, ajuste a URL:

```kotlin
private const val BASE_URL = "http://SEU_IP_LOCAL:8080/"

http://192.168.1.14:8080/

▶️ Como rodar
Clone o projeto

📱 Funcionalidades atuais
Login com API
Persistência de sessão
Interceptor JWT
Navegação básica (Login → Dashboard)
Logout


🧪 Próximos passos
 Navegação com NavHost
 Tela de produtos
 Tela de pedidos
 Integração com mesas (ServiceTable)
 Pagamentos
 Controle de acesso por role (RBAC)
 Tratamento global de erros
 Design system (UI)