package bench

import org.openjdk.jmh.annotations.*

// Something like HashMap<String,String>
class Assign_Holder {
    @Volatile private var value :Any = ""

    operator fun set(
        @Suppress("UNUSED_PARAMETER") key:String,
        value:Any){
       this.value = value
    }
}

class Assign_PrefKey(private val key: String) {
    fun put(holder: Assign_Holder, value: String) {
        holder[key] = value
    }
}

fun Assign_Holder.putA(pref: Assign_PrefKey, value: String): Assign_Holder {
    pref.put(this, value)
    return this
}

fun Assign_Holder.putB(pref: Assign_PrefKey, value: String): Assign_Holder =
    apply {
        pref.put(this, value)
    }

class HashMap_Holder : HashMap<String,String>()

class HashMap_PrefKey(private val key: String) {
    fun put(holder: HashMap_Holder, value: String) {
        holder[key] = value
    }
}

fun HashMap_Holder.putA(pref: HashMap_PrefKey, value: String): HashMap_Holder {
    pref.put(this, value)
    return this
}

fun HashMap_Holder.putB(pref: HashMap_PrefKey, value: String): HashMap_Holder =
    apply {
        pref.put(this, value)
    }


// Benchmark classes should not be final. // [jmh.bench.Test1]

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 15, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@Suppress("unused")
open class Test1 {

    private var Assign_holder= Assign_Holder()
    private var Assign_pref= Assign_PrefKey("foo")

    private var HashMap_holder= HashMap_Holder()
    private var HashMap_pref= HashMap_PrefKey("foo")

    @Benchmark
    fun Assign_usePutA(){
        Assign_holder.putA(Assign_pref, "zap")
    }

    @Benchmark
    fun Assign_usePutB(){
        Assign_holder.putB(Assign_pref, "zap")
    }

    @Benchmark
    fun HashMap_usePutA(){
        HashMap_holder.putA(HashMap_pref, "zap")
    }

    @Benchmark
    fun HashMap_usePutB(){
        HashMap_holder.putB(HashMap_pref, "zap")
    }

}
