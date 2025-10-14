# 🧩 Flutter Template Generator

![JetBrains Plugin](https://img.shields.io/badge/JetBrains-Plugin-blueviolet?logo=intellijidea)
![Platform](https://img.shields.io/badge/Platform-Android%20Studio%20%7C%20IntelliJ-blue)
![Language](https://img.shields.io/badge/Language-Kotlin-orange)
![Flutter](https://img.shields.io/badge/Flutter-Compatible-success)
![License](https://img.shields.io/badge/License-MIT-green)

> ⚡ Gere estruturas e templates Flutter automaticamente no Android Studio.

O **Flutter Template Generator** é um plugin para **Android Studio** e **IntelliJ IDEA** que automatiza a criação de **pastas, classes e arquiteturas** Flutter (como **MVVM**), reduzindo o trabalho manual e acelerando o desenvolvimento.

---

## 🎥 Demonstração

> ✨ Veja o plugin em ação (adicione um GIF curto aqui):

<div align="center">
  <img src="docs/images/demo.gif" alt="Demonstração do Flutter Template Generator" width="700">
</div>

---

## 🚀 Recursos principais

| Recurso | Descrição |
|----------|------------|
| 📁 **Generate Folder** | Cria rapidamente uma nova pasta Flutter com estrutura básica. |
| 🏗️ **Generate MVVM Architecture** | Gera automaticamente uma estrutura de pastas e arquivos seguindo o padrão **MVVM**. |
| 🧠 **Live Templates e Internal Templates** | Crie classes, interfaces, testes e widgets Flutter com base em templates prontos. |
| ⚙️ **Ações de intenção (Intention Actions)** | Gere arquivos e implemente classes diretamente pelo menu contextual. |

---

## 🧩 Ações disponíveis

| Nome da Ação | Classe | Descrição | Ícone |
|---------------|---------|------------|-------|
| **Generate MVVM Architecture** | `GenerateFolderStructureActionInMVVM` | Cria estrutura de pastas MVVM (Model, View, ViewModel). | 🗂️ `generate_folder_mvvm.svg` |
| **Generate Folder** | `GenerateFolder` | Cria uma nova pasta padrão Flutter. | 📂 `GenerateFolder.svg` |
| **Create Dart Class File** | `CreateDartClassFileAction` | Gera uma nova classe Dart com base em um template interno. | 💡 — |

---

## 🧱 Estrutura do projeto

