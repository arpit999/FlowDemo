# FlowDemo
Kotlin Flow operations: refer brach for each operation
<ul>
<li>Why we need Flow -> <i>why_flow</i></li>
<li>Basic example</li>
<li>Catch exceptions</li>
<li>Operators/Intermediaries</li>
<li>Switching context</li>
</ul>

<h3>INTRODUCTION TO FLOW</h3>

 Flow is used for transporting stream data (Continuous data) asynchronously. It is work with coroutines and
 There are two types of flow in kotlin
  1) <b>Hot Flow(Channel/SharedFlow)</b> : Continuously produce data even if there is no consumer. If consumer join hot flow in middle of the emission it will receive latest emission and lose previous emission. Can't think of any use case in android where we need that. If hot flow is running and there is no consumer than there would resource wastage.
 2) <b>Cold Flow(StateFlow)</b> : In most cases in android we use cold flow. If cold flow is running and there is no consumer then it will not produce any emission until there is a consumer. Cold floe provide emission from start even consumer join in middle.
 
 Flow have three main components:
 1) <b>Producer</b>: That produce the emissions for each consumer.
 2) <b>(Optional) Intermediaries/Operators</b> : It is provide opportunity to modify emission values before consuming them. Example: onEach, onComplete, map, buffer, etc.
 3) <b>Consumer</b> : That collect emitted values from the producer.
 

<b>WHY FLOW:</b> refer branch <i>why_flow</i>
Here is why we need flow with suspend functions. Here in this example we are making api calls for single user id and creating list of users so we can access them in our UI.
If you notice that we are receiving list of users after 5 seconds. While if we use flow instead than we would be able to access user when that api response are available. So we don't need to wait for whole list of users.

<b>Branch: simple_flow</b>
- We can add multiple collectors to flow. If we are using cold flow and collector join in middle of emissions then it will receive values from start and continue receive emissions till end. 

- There is no specific method to cancel flow instead we are using coroutine scope to cancel collectors and it will stop producing emissions. In short, If there is no collectors than producer will not produce any emissions. 

 <b>coroutineScope.cancel():</b> It will cancel the all coroutines for that scope but we won't be able to start that coroutins again. Because it is also cancel that job we are in. 
 
 <b>coroutineScope.coroutineContext.cancelChildren():</b> It will cancel all the children from that scope and we will be able to start that coroutine again. 
 
Note from SO: Instead of using CoroutineScope for cancelling all of the launched jobs in it, you may want to use the underlying CoroutineContext with its cancelChildren() method which doesn't affect the Job state (which is not true for plain cancel() method) and allows to continue launching new coroutines after being invoked.
