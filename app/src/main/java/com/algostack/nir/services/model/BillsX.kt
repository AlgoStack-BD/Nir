package com.algostack.nir.services.model

data class BillsX(
    val electricBill: Boolean,
    val gasBill: Boolean,
    val otherBills: String,
    val waterBill: Boolean
)