package bench

import org.openjdk.jmh.annotations.*

// Something like HashMap<String,String>
class Holder {
    @Volatile private var value :Any = ""

    operator fun set(
        @Suppress("UNUSED_PARAMETER") key:String,
        value:Any){
       this.value = value
    }
}

class PrefKey(private val key: String) {
    fun put(holder: Holder, value: String) {
        holder[key] = value
    }
}

fun Holder.putA(pref: PrefKey, value: String): Holder {
    pref.put(this, value)
    return this
}

fun Holder.putB(pref: PrefKey, value: String): Holder =
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

    private var holder= Holder()
    private var pref= PrefKey("foo")

    @Benchmark
    fun usePutA(){
        holder.putA(pref, "zap")
    }

    @Benchmark
    fun usePutB(){
        holder.putB(pref, "zap")
    }
}
