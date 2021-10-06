package br.com.quanto.tsp.template.testutils

import org.testcontainers.vault.VaultContainer

class QuantoVaultContainer(imageName: String) : VaultContainer<QuantoVaultContainer>(imageName)
