package rockyouapi.utils

/** Available argument type. Used to define type when call responded by invalid type errors. */
internal enum class ArgumentType(val typeName: String) {
    INT("Int"),
    LONG("Long")
}