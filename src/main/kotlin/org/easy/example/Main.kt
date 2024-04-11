package org.easy.example

import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.easy.example.model.Parameter
import org.easy.example.model.RpcRequestBody

fun main() {
    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        allowStructuredMapKeys = true
        serializersModule = SerializersModule {
            polymorphic(List::class) {
                ListSerializer(Parameter.serializer())
            }
            polymorphic(Parameter::class) {
                subclass(Parameter.CallParameter::class, Parameter.CallParameter.serializer())
                subclass(Parameter.StringParameter::class, Parameter.StringParameter.serializer())
                subclass(Parameter.IntListParameter::class, Parameter.IntListParameter.serializer())
            }
        }
    }

    val body = RpcRequestBody(
        id = 1, jsonrpc = "2.0", method = "eth_call", params = listOf(
            Parameter.CallParameter(
                data = "0x5ec88c7900000000000000000000000081080a7e991bcdddba8c2302a70f45d6bd369ab5",
                from = "0x81080a7e991bcDdDBA8C2302A70f45d6Bd369Ab5",
                to = "0xb3831584acb95ED9cCb0C11f677B5AD01DeaeEc0"
            ), Parameter.IntListParameter(items = listOf(2, 5, 8)), Parameter.StringParameter(content = "latest")
        )
    )

    val result = json.encodeToString(RpcRequestBody.serializer(), body)
    println(result)

    println("=======================")


    val jsonStr = """
        {
            "jsonrpc": "2.0",
            "method": "eth_call",
            "params": [
                {
                    "data": "0x5ec88c7900000000000000000000000081080a7e991bcdddba8c2302a70f45d6bd369ab5",
                    "from": "0x81080a7e991bcDdDBA8C2302A70f45d6Bd369Ab5",
                    "to": "0xb3831584acb95ED9cCb0C11f677B5AD01DeaeEc0"
                },
                [
                    2,
                    5,
                    8
                ],
                "latest"
            ],
            "id": 1
        }
    """


    val obj = json.decodeFromString<RpcRequestBody>(jsonStr)
    println("$obj")

}