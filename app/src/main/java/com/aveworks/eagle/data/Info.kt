package com.aveworks.eagle.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown=true)
data class Info(
    @field:JsonProperty("symbol_local") val local: HashMap<String, Any>,
)