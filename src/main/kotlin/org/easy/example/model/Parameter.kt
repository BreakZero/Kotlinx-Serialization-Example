package org.easy.example.model

import kotlinx.serialization.Serializable

@Serializable(with = ParameterSerialize::class)
internal sealed interface Parameter {
    @Serializable
    data class CallParameter(
        val data: String,
        val from: String,
        val to: String
    ) : Parameter

    @Serializable(with = IntListParameterSerializer::class)
    data class IntListParameter(
        val items: List<Int>
    ) : Parameter

    @Serializable(with = StringParameterSerializer::class)
    data class StringParameter(
        val content: String
    ) : Parameter
}