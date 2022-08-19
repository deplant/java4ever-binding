package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import lombok.*;
import java.util.stream.*;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 *  <h1>net</h1>
 *  Contains methods of "net" module.

 *  Network access.
 *  @version EVER-SDK 1.37.0
 */
public class Net {


    /**
    * 
    * @param path 
    * @param direction 
    */
    public record OrderBy(@NonNull String path, @NonNull SortDirection direction) {}
    public enum SortDirection {
        
        
        ASC,

        
        DESC
    }
    public interface ParamsOfQueryOperation {


    /**
    * 
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
    * @param filter Collection filter
    * @param result Projection (result) string
    * @param order Sorting order
    * @param limit Number of documents to return
    */
    public record QueryCollection(@NonNull String collection, Map<String,Object> filter, @NonNull String result, OrderBy[] order, Number limit) implements ParamsOfQueryOperation {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
    * @param filter Collection filter
    * @param result Projection (result) string
    * @param timeout Query timeout
    */
    public record WaitForCollection(@NonNull String collection, Map<String,Object> filter, @NonNull String result, Number timeout) implements ParamsOfQueryOperation {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
    * @param filter Collection filter
    * @param fields Projection (result) string
    */
    public record AggregateCollection(@NonNull String collection, Map<String,Object> filter, FieldAggregation[] fields) implements ParamsOfQueryOperation {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param account Account address
    * @param result Projection (result) string
    * @param first Number of counterparties to return
    * @param after `cursor` field of the last received result
    */
    public record QueryCounterparties(@NonNull String account, @NonNull String result, Number first, String after) implements ParamsOfQueryOperation {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param field Dot separated path to the field
    * @param fn Aggregation function that must be applied to field values
    */
    public record FieldAggregation(@NonNull String field, @NonNull AggregationFn fn) {}
    public enum AggregationFn {
        
        /**
        * Returns count of filtered record
        */
        COUNT,

        /**
        * Returns the minimal value for a field in filtered records
        */
        MIN,

        /**
        * Returns the maximal value for a field in filtered records
        */
        MAX,

        /**
        * Returns a sum of values for a field in filtered records
        */
        SUM,

        /**
        * Returns an average value for a field in filtered records
        */
        AVERAGE
    }

    /**
    * 
    * @param id Transaction id.
    * @param inMsg In message id.
    * @param outMsgs Out message ids.
    * @param accountAddr Account address.
    * @param totalFees Transactions total fees.
    * @param aborted Aborted flag.
    * @param exitCode Compute phase exit code.
    */
    public record TransactionNode(@NonNull String id, @NonNull String inMsg, @NonNull String[] outMsgs, @NonNull String accountAddr, @NonNull String totalFees, @NonNull Boolean aborted, Number exitCode) {}

    /**
    * 
    * @param id Message id.
    * @param srcTransactionId Source transaction id. This field is missing for an external inbound messages.
    * @param dstTransactionId Destination transaction id. This field is missing for an external outbound messages.
    * @param src Source address.
    * @param dst Destination address.
    * @param value Transferred tokens value.
    * @param bounce Bounce flag.
    * @param decodedBody Decoded body. Library tries to decode message body using provided `params.abi_registry`.This field will be missing if none of the provided abi can be used to decode.
    */
    public record MessageNode(@NonNull String id, String srcTransactionId, String dstTransactionId, String src, String dst, String value, @NonNull Boolean bounce, Abi.DecodedMessageBody decodedBody) {}

    /**
    * 
    * @param query GraphQL query text.
    * @param variables Variables used in query. Must be a map with named values that can be used in query.
    */
    public record ParamsOfQuery(@NonNull String query, Map<String,Object> variables) {}

    /**
    * 
    * @param result Result provided by DAppServer.
    */
    public record ResultOfQuery(@NonNull Map<String,Object> result) {}

    /**
    * 
    * @param operations List of query operations that must be performed per single fetch.
    */
    public record ParamsOfBatchQuery(@NonNull ParamsOfQueryOperation[] operations) {}

    /**
    * 
    * @param results Result values for batched queries. Returns an array of values. Each value corresponds to `queries` item.
    */
    public record ResultOfBatchQuery(@NonNull Map<String,Object>[] results) {}

    /**
    * 
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
    * @param filter Collection filter
    * @param result Projection (result) string
    * @param order Sorting order
    * @param limit Number of documents to return
    */
    public record ParamsOfQueryCollection(@NonNull String collection, Map<String,Object> filter, @NonNull String result, OrderBy[] order, Number limit) {}

    /**
    * 
    * @param result Objects that match the provided criteria
    */
    public record ResultOfQueryCollection(@NonNull Map<String,Object>[] result) {}

    /**
    * 
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
    * @param filter Collection filter
    * @param fields Projection (result) string
    */
    public record ParamsOfAggregateCollection(@NonNull String collection, Map<String,Object> filter, FieldAggregation[] fields) {}

    /**
    * 
    * @param values Values for requested fields. Returns an array of strings. Each string refers to the corresponding `fields` item.Numeric value is returned as a decimal string representations.
    */
    public record ResultOfAggregateCollection(@NonNull Map<String,Object> values) {}

    /**
    * 
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
    * @param filter Collection filter
    * @param result Projection (result) string
    * @param timeout Query timeout
    */
    public record ParamsOfWaitForCollection(@NonNull String collection, Map<String,Object> filter, @NonNull String result, Number timeout) {}

    /**
    * 
    * @param result First found object that matches the provided criteria
    */
    public record ResultOfWaitForCollection(@NonNull Map<String,Object> result) {}

    /**
    * 
    * @param handle Subscription handle. Must be closed with `unsubscribe`
    */
    public record ResultOfSubscribeCollection(@NonNull Number handle) {}

    /**
    * 
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
    * @param filter Collection filter
    * @param result Projection (result) string
    */
    public record ParamsOfSubscribeCollection(@NonNull String collection, Map<String,Object> filter, @NonNull String result) {}

    /**
    * 
    * @param subscription GraphQL subscription text.
    * @param variables Variables used in subscription. Must be a map with named values that can be used in query.
    */
    public record ParamsOfSubscribe(@NonNull String subscription, Map<String,Object> variables) {}

    /**
    * 
    * @param address Account address
    */
    public record ParamsOfFindLastShardBlock(@NonNull String address) {}

    /**
    * 
    * @param blockId Account shard last block ID
    */
    public record ResultOfFindLastShardBlock(@NonNull String blockId) {}

    /**
    * 
    * @param endpoints List of endpoints provided by server
    */
    public record EndpointsSet(@NonNull String[] endpoints) {}

    /**
    * 
    * @param query Current query endpoint
    * @param endpoints List of all endpoints used by client
    */
    public record ResultOfGetEndpoints(@NonNull String query, @NonNull String[] endpoints) {}

    /**
    * 
    * @param account Account address
    * @param result Projection (result) string
    * @param first Number of counterparties to return
    * @param after `cursor` field of the last received result
    */
    public record ParamsOfQueryCounterparties(@NonNull String account, @NonNull String result, Number first, String after) {}

    /**
    * 
    * @param inMsg Input message id.
    * @param abiRegistry List of contract ABIs that will be used to decode message bodies. Library will try to decode each returned message body using any ABI from the registry.
    * @param timeout Timeout used to limit waiting time for the missing messages and transaction. If some of the following messages and transactions are missing yetThe maximum waiting time is regulated by this option.<p>Default value is 60000 (1 min).
    */
    public record ParamsOfQueryTransactionTree(@NonNull String inMsg, Abi.ABI[] abiRegistry, Number timeout) {}

    /**
    * 
    * @param messages Messages.
    * @param transactions Transactions.
    */
    public record ResultOfQueryTransactionTree(@NonNull MessageNode[] messages, @NonNull TransactionNode[] transactions) {}

    /**
    * 
    * @param startTime Starting time to iterate from. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &gt;= `start_time`.Otherwise the iteration starts from zero state.<p>Must be specified in seconds.
    * @param endTime Optional end time to iterate for. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &lt; `end_time`.Otherwise the iteration never stops.<p>Must be specified in seconds.
    * @param shardFilter Shard prefix filter. If the application specifies this parameter and it is not the empty arraythen the iteration will include items related to accounts that belongs tothe specified shard prefixes.Shard prefix must be represented as a string "workchain:prefix".Where `workchain` is a signed integer and the `prefix` if a hexadecimalrepresentation if the 64-bit unsigned integer with tagged shard prefix.For example: "0:3800000000000000".
    * @param result Projection (result) string. List of the fields that must be returned for iterated items.This field is the same as the `result` parameter ofthe `query_collection` function.Note that iterated items can contains additional fields that arenot requested in the `result`.
    */
    public record ParamsOfCreateBlockIterator(Number startTime, Number endTime, String[] shardFilter, String result) {}

    /**
    * 
    * @param handle Iterator handle. Must be removed using `remove_iterator`when it is no more needed for the application.
    */
    public record RegisteredIterator(@NonNull Number handle) {}

    /**
    * 
    * @param resumeState Iterator state from which to resume. Same as value returned from `iterator_next`.
    */
    public record ParamsOfResumeBlockIterator(@NonNull Map<String,Object> resumeState) {}

    /**
    * 
    * @param startTime Starting time to iterate from. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &gt;= `start_time`.Otherwise the iteration starts from zero state.<p>Must be specified in seconds.
    * @param endTime Optional end time to iterate for. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &lt; `end_time`.Otherwise the iteration never stops.<p>Must be specified in seconds.
    * @param shardFilter Shard prefix filters. If the application specifies this parameter and it is not an empty arraythen the iteration will include items related to accounts that belongs tothe specified shard prefixes.Shard prefix must be represented as a string "workchain:prefix".Where `workchain` is a signed integer and the `prefix` if a hexadecimalrepresentation if the 64-bit unsigned integer with tagged shard prefix.For example: "0:3800000000000000".Account address conforms to the shard filter ifit belongs to the filter workchain and the first bits of address match tothe shard prefix. Only transactions with suitable account addresses are iterated.
    * @param accountsFilter Account address filter. Application can specify the list of accounts for whichit wants to iterate transactions.<p>If this parameter is missing or an empty list then the library iteratestransactions for all accounts that pass the shard filter.<p>Note that the library doesn't detect conflicts between the account filter and the shard filterif both are specified.So it is an application responsibility to specify the correct filter combination.
    * @param result Projection (result) string. List of the fields that must be returned for iterated items.This field is the same as the `result` parameter ofthe `query_collection` function.Note that iterated items can contain additional fields that arenot requested in the `result`.
    * @param includeTransfers Include `transfers` field in iterated transactions. If this parameter is `true` then each transaction contains field`transfers` with list of transfer. See more about this structure in function description.
    */
    public record ParamsOfCreateTransactionIterator(Number startTime, Number endTime, String[] shardFilter, String[] accountsFilter, String result, Boolean includeTransfers) {}

    /**
    * 
    * @param resumeState Iterator state from which to resume. Same as value returned from `iterator_next`.
    * @param accountsFilter Account address filter. Application can specify the list of accounts for whichit wants to iterate transactions.<p>If this parameter is missing or an empty list then the library iteratestransactions for all accounts that passes the shard filter.<p>Note that the library doesn't detect conflicts between the account filter and the shard filterif both are specified.So it is the application's responsibility to specify the correct filter combination.
    */
    public record ParamsOfResumeTransactionIterator(@NonNull Map<String,Object> resumeState, String[] accountsFilter) {}

    /**
    * 
    * @param iterator Iterator handle
    * @param limit Maximum count of the returned items. If value is missing or is less than 1 the library uses 1.
    * @param returnResumeState Indicates that function must return the iterator state that can be used for resuming iteration.
    */
    public record ParamsOfIteratorNext(@NonNull Number iterator, Number limit, Boolean returnResumeState) {}

    /**
    * 
    * @param items Next available items. Note that `iterator_next` can return an empty items and `has_more` equals to `true`.In this case the application have to continue iteration.Such situation can take place when there is no data yet butthe requested `end_time` is not reached.
    * @param hasMore Indicates that there are more available items in iterated range.
    * @param resumeState Optional iterator state that can be used for resuming iteration. This field is returned only if the `return_resume_state` parameteris specified.<p>Note that `resume_state` corresponds to the iteration positionafter the returned items.
    */
    public record ResultOfIteratorNext(@NonNull Map<String,Object>[] items, @NonNull Boolean hasMore, Map<String,Object> resumeState) {}
    /**
    * <h2>net.query</h2>
    * Performs DAppServer GraphQL query.
    * @param query GraphQL query text. 
    * @param variables Variables used in query. Must be a map with named values that can be used in query.
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfQuery}
    */
    public static ResultOfQuery query(@NonNull Context ctx, @NonNull String query,  Map<String,Object> variables)  throws JsonProcessingException {
        return ctx.call("net.query", new ParamsOfQuery(query, variables), ResultOfQuery.class);
    }

    /**
    * <h2>net.batch_query</h2>
    * Performs multiple queries per single fetch.
    * @param operations List of query operations that must be performed per single fetch. 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfBatchQuery}
    */
    public static ResultOfBatchQuery batchQuery(@NonNull Context ctx, @NonNull ParamsOfQueryOperation[] operations)  throws JsonProcessingException {
        return ctx.call("net.batch_query", new ParamsOfBatchQuery(operations), ResultOfBatchQuery.class);
    }

    /**
    * <h2>net.query_collection</h2>
    * Queries collection data Queries data that satisfies the `filter` conditions,limits the number of returned records and orders them.The projection fields are limited to `result` fields
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures) 
    * @param filter Collection filter 
    * @param result Projection (result) string 
    * @param order Sorting order 
    * @param limit Number of documents to return 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfQueryCollection}
    */
    public static ResultOfQueryCollection queryCollection(@NonNull Context ctx, @NonNull String collection,  Map<String,Object> filter, @NonNull String result,  OrderBy[] order,  Number limit)  throws JsonProcessingException {
        return ctx.call("net.query_collection", new ParamsOfQueryCollection(collection, filter, result, order, limit), ResultOfQueryCollection.class);
    }

    /**
    * <h2>net.aggregate_collection</h2>
    * Aggregates collection data. Aggregates values from the specified `fields` for recordsthat satisfies the `filter` conditions,
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures) 
    * @param filter Collection filter 
    * @param fields Projection (result) string 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfAggregateCollection}
    */
    public static ResultOfAggregateCollection aggregateCollection(@NonNull Context ctx, @NonNull String collection,  Map<String,Object> filter,  FieldAggregation[] fields)  throws JsonProcessingException {
        return ctx.call("net.aggregate_collection", new ParamsOfAggregateCollection(collection, filter, fields), ResultOfAggregateCollection.class);
    }

    /**
    * <h2>net.wait_for_collection</h2>
    * Returns an object that fulfills the conditions or waits for its appearance Triggers only once.If object that satisfies the `filter` conditionsalready exists - returns it immediately.If not - waits for insert/update of data within the specified `timeout`,and returns it.The projection fields are limited to `result` fields
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures) 
    * @param filter Collection filter 
    * @param result Projection (result) string 
    * @param timeout Query timeout 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfWaitForCollection}
    */
    public static ResultOfWaitForCollection waitForCollection(@NonNull Context ctx, @NonNull String collection,  Map<String,Object> filter, @NonNull String result,  Number timeout)  throws JsonProcessingException {
        return ctx.call("net.wait_for_collection", new ParamsOfWaitForCollection(collection, filter, result, timeout), ResultOfWaitForCollection.class);
    }

    /**
    * <h2>net.unsubscribe</h2>
    * Cancels a subscription Cancels a subscription specified by its handle.
    * @param handle Subscription handle. Must be closed with `unsubscribe`
    * @return {@link tech.deplant.java4ever.binding.Net.Void}
    */
    public static Void unsubscribe(@NonNull Context ctx, @NonNull Number handle)  throws JsonProcessingException {
        return ctx.call("net.unsubscribe", new ResultOfSubscribeCollection(handle), Void.class);
    }

    /**
    * <h2>net.subscribe_collection</h2>
    * Creates a collection subscription Triggers for each insert/update of data that satisfiesthe `filter` conditions.The projection fields are limited to `result` fields.<p>The subscription is a persistent communication channel betweenclient and Free TON Network.All changes in the blockchain will be reflected in realtime.Changes means inserts and updates of the blockchain entities.<p>### Important Notes on Subscriptions<p>Unfortunately sometimes the connection with the network brakes down.In this situation the library attempts to reconnect to the network.This reconnection sequence can take significant time.All of this time the client is disconnected from the network.<p>Bad news is that all blockchain changes that happened whilethe client was disconnected are lost.<p>Good news is that the client report errors to the callback whenit loses and resumes connection.<p>So, if the lost changes are important to the application thenthe application must handle these error reports.<p>Library reports errors with `responseType` == 101and the error object passed via `params`.<p>When the library has successfully reconnectedthe application receives callback with`responseType` == 101 and `params.code` == 614 (NetworkModuleResumed).<p>Application can use several ways to handle this situation:- If application monitors changes for the single blockchainobject (for example specific account):  applicationcan perform a query for this object and handle actual data as aregular data from the subscription.- If application monitors sequence of some blockchain objects(for example transactions of the specific account): application mustrefresh all cached (or visible to user) lists where this sequences presents.
    * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures) 
    * @param filter Collection filter 
    * @param result Projection (result) string 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfSubscribeCollection}
    */
    public static ResultOfSubscribeCollection subscribeCollection(@NonNull Context ctx, @NonNull String collection,  Map<String,Object> filter, @NonNull String result, Consumer<SubscribeCollectionEvent> consumer)  throws JsonProcessingException {
        return ctx.callEvent("net.subscribe_collection", new ParamsOfSubscribeCollection(collection, filter, result), consumer, ResultOfSubscribeCollection.class);
    }

    /**
    * <h2>net.subscribe</h2>
    * Creates a subscription The subscription is a persistent communication channel betweenclient and Everscale Network.<p>### Important Notes on Subscriptions<p>Unfortunately sometimes the connection with the network brakes down.In this situation the library attempts to reconnect to the network.This reconnection sequence can take significant time.All of this time the client is disconnected from the network.<p>Bad news is that all changes that happened whilethe client was disconnected are lost.<p>Good news is that the client report errors to the callback whenit loses and resumes connection.<p>So, if the lost changes are important to the application thenthe application must handle these error reports.<p>Library reports errors with `responseType` == 101and the error object passed via `params`.<p>When the library has successfully reconnectedthe application receives callback with`responseType` == 101 and `params.code` == 614 (NetworkModuleResumed).<p>Application can use several ways to handle this situation:- If application monitors changes for the singleobject (for example specific account):  applicationcan perform a query for this object and handle actual data as aregular data from the subscription.- If application monitors sequence of some objects(for example transactions of the specific account): application mustrefresh all cached (or visible to user) lists where this sequences presents.
    * @param subscription GraphQL subscription text. 
    * @param variables Variables used in subscription. Must be a map with named values that can be used in query.
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfSubscribeCollection}
    */
    public static ResultOfSubscribeCollection subscribe(@NonNull Context ctx, @NonNull String subscription,  Map<String,Object> variables, Consumer<SubscribeEvent> consumer)  throws JsonProcessingException {
        return ctx.callEvent("net.subscribe", new ParamsOfSubscribe(subscription, variables), consumer, ResultOfSubscribeCollection.class);
    }

    /**
    * <h2>net.suspend</h2>
    * Suspends network module to stop any network activity
    * @return {@link tech.deplant.java4ever.binding.Net.Void}
    */
    public static Void suspend(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("net.suspend", null, Void.class);
    }

    /**
    * <h2>net.resume</h2>
    * Resumes network module to enable network activity
    * @return {@link tech.deplant.java4ever.binding.Net.Void}
    */
    public static Void resume(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("net.resume", null, Void.class);
    }

    /**
    * <h2>net.find_last_shard_block</h2>
    * Returns ID of the last block in a specified account shard
    * @param address Account address 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfFindLastShardBlock}
    */
    public static ResultOfFindLastShardBlock findLastShardBlock(@NonNull Context ctx, @NonNull String address)  throws JsonProcessingException {
        return ctx.call("net.find_last_shard_block", new ParamsOfFindLastShardBlock(address), ResultOfFindLastShardBlock.class);
    }

    /**
    * <h2>net.fetch_endpoints</h2>
    * Requests the list of alternative endpoints from server
    * @return {@link tech.deplant.java4ever.binding.Net.EndpointsSet}
    */
    public static EndpointsSet fetchEndpoints(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("net.fetch_endpoints", null, EndpointsSet.class);
    }

    /**
    * <h2>net.set_endpoints</h2>
    * Sets the list of endpoints to use on reinit
    * @param endpoints List of endpoints provided by server 
    * @return {@link tech.deplant.java4ever.binding.Net.Void}
    */
    public static Void setEndpoints(@NonNull Context ctx, @NonNull String[] endpoints)  throws JsonProcessingException {
        return ctx.call("net.set_endpoints", new EndpointsSet(endpoints), Void.class);
    }

    /**
    * <h2>net.get_endpoints</h2>
    * Requests the list of alternative endpoints from server
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfGetEndpoints}
    */
    public static ResultOfGetEndpoints getEndpoints(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("net.get_endpoints", null, ResultOfGetEndpoints.class);
    }

    /**
    * <h2>net.query_counterparties</h2>
    * Allows to query and paginate through the list of accounts that the specified account has interacted with, sorted by the time of the last internal message between accounts *Attention* this query retrieves data from 'Counterparties' service which is not supported inthe opensource version of DApp Server (and will not be supported) as well as in Evernode SE (will be supported in SE in future),but is always accessible via <a target="_blank" href="EVER OS Clouds">EVER OS Clouds</a>(../ton-os-api/networks.md)
    * @param account Account address 
    * @param result Projection (result) string 
    * @param first Number of counterparties to return 
    * @param after `cursor` field of the last received result 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfQueryCollection}
    */
    public static ResultOfQueryCollection queryCounterparties(@NonNull Context ctx, @NonNull String account, @NonNull String result,  Number first,  String after)  throws JsonProcessingException {
        return ctx.call("net.query_counterparties", new ParamsOfQueryCounterparties(account, result, first, after), ResultOfQueryCollection.class);
    }

    /**
    * <h2>net.query_transaction_tree</h2>
    * Returns a tree of transactions triggered by a specific message. Performs recursive retrieval of a transactions tree produced by a specific message:in_msg -&gt; dst_transaction -&gt; out_messages -&gt; dst_transaction -&gt; ...If the chain of transactions execution is in progress while the function is running,it will wait for the next transactions to appear until the full tree or more than 50 transactionsare received.<p>All the retrieved messages and transactions are includedinto `result.messages` and `result.transactions` respectively.<p>Function reads transactions layer by layer, by pages of 20 transactions.<p>The retrieval prosess goes like this:Let's assume we have an infinite chain of transactions and each transaction generates 5 messages.1. Retrieve 1st message (input parameter) and corresponding transaction - put it into result.It is the first level of the tree of transactions - its root.Retrieve 5 out message ids from the transaction for next steps.2. Retrieve 5 messages and corresponding transactions on the 2nd layer. Put them into result.Retrieve 5*5 out message ids from these transactions for next steps3. Retrieve 20 (size of the page) messages and transactions (3rd layer) and 20*5=100 message ids (4th layer).4. Retrieve the last 5 messages and 5 transactions on the 3rd layer + 15 messages and transactions (of 100) from the 4th layer+ 25 message ids of the 4th layer + 75 message ids of the 5th layer.5. Retrieve 20 more messages and 20 more transactions of the 4th layer + 100 more message ids of the 5th layer.6. Now we have 1+5+20+20+20 = 66 transactions, which is more than 50. Function exits with the tree of1m-&gt;1t-&gt;5m-&gt;5t-&gt;25m-&gt;25t-&gt;35m-&gt;35t. If we see any message ids in the last transactions out_msgs, which don't havecorresponding messages in the function result, it means that the full tree was not received and we need to continue iteration.<p>To summarize, it is guaranteed that each message in `result.messages` has the corresponding transactionin the `result.transactions`.But there is no guarantee that all messages from transactions `out_msgs` arepresented in `result.messages`.So the application has to continue retrieval for missing messages if it requires.
    * @param inMsg Input message id. 
    * @param abiRegistry List of contract ABIs that will be used to decode message bodies. Library will try to decode each returned message body using any ABI from the registry. 
    * @param timeout Timeout used to limit waiting time for the missing messages and transaction. If some of the following messages and transactions are missing yetThe maximum waiting time is regulated by this option.<p>Default value is 60000 (1 min).
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfQueryTransactionTree}
    */
    public static ResultOfQueryTransactionTree queryTransactionTree(@NonNull Context ctx, @NonNull String inMsg,  Abi.ABI[] abiRegistry,  Number timeout)  throws JsonProcessingException {
        return ctx.call("net.query_transaction_tree", new ParamsOfQueryTransactionTree(inMsg, abiRegistry, timeout), ResultOfQueryTransactionTree.class);
    }

    /**
    * <h2>net.create_block_iterator</h2>
    * Creates block iterator. Block iterator uses robust iteration methods that guaranties that everyblock in the specified range isn't missed or iterated twice.<p>Iterated range can be reduced with some filters:- `start_time` – the bottom time range. Only blocks with `gen_utime`more or equal to this value is iterated. If this parameter is omitted then there isno bottom time edge, so all blocks since zero state is iterated.- `end_time` – the upper time range. Only blocks with `gen_utime`less then this value is iterated. If this parameter is omitted then there isno upper time edge, so iterator never finishes.- `shard_filter` – workchains and shard prefixes that reduce the set of interestingblocks. Block conforms to the shard filter if it belongs to the filter workchainand the first bits of block's `shard` fields matches to the shard prefix.Only blocks with suitable shard are iterated.<p>Items iterated is a JSON objects with block data. The minimal set of returnedfields is:```textidgen_utimeworkchain_idshardafter_splitafter_mergeprev_ref {    root_hash}prev_alt_ref {    root_hash}```Application can request additional fields in the `result` parameter.<p>Application should call the `remove_iterator` when iterator is no longer required.
    * @param startTime Starting time to iterate from. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &gt;= `start_time`.Otherwise the iteration starts from zero state.<p>Must be specified in seconds.
    * @param endTime Optional end time to iterate for. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &lt; `end_time`.Otherwise the iteration never stops.<p>Must be specified in seconds.
    * @param shardFilter Shard prefix filter. If the application specifies this parameter and it is not the empty arraythen the iteration will include items related to accounts that belongs tothe specified shard prefixes.Shard prefix must be represented as a string "workchain:prefix".Where `workchain` is a signed integer and the `prefix` if a hexadecimalrepresentation if the 64-bit unsigned integer with tagged shard prefix.For example: "0:3800000000000000".
    * @param result Projection (result) string. List of the fields that must be returned for iterated items.This field is the same as the `result` parameter ofthe `query_collection` function.Note that iterated items can contains additional fields that arenot requested in the `result`.
    * @return {@link tech.deplant.java4ever.binding.Net.RegisteredIterator}
    */
    public static RegisteredIterator createBlockIterator(@NonNull Context ctx,  Number startTime,  Number endTime,  String[] shardFilter,  String result)  throws JsonProcessingException {
        return ctx.call("net.create_block_iterator", new ParamsOfCreateBlockIterator(startTime, endTime, shardFilter, result), RegisteredIterator.class);
    }

    /**
    * <h2>net.resume_block_iterator</h2>
    * Resumes block iterator. The iterator stays exactly at the same position where the `resume_state` was catched.<p>Application should call the `remove_iterator` when iterator is no longer required.
    * @param resumeState Iterator state from which to resume. Same as value returned from `iterator_next`.
    * @return {@link tech.deplant.java4ever.binding.Net.RegisteredIterator}
    */
    public static RegisteredIterator resumeBlockIterator(@NonNull Context ctx, @NonNull Map<String,Object> resumeState)  throws JsonProcessingException {
        return ctx.call("net.resume_block_iterator", new ParamsOfResumeBlockIterator(resumeState), RegisteredIterator.class);
    }

    /**
    * <h2>net.create_transaction_iterator</h2>
    * Creates transaction iterator. Transaction iterator uses robust iteration methods that guaranty that everytransaction in the specified range isn't missed or iterated twice.<p>Iterated range can be reduced with some filters:- `start_time` – the bottom time range. Only transactions with `now`more or equal to this value are iterated. If this parameter is omitted then there isno bottom time edge, so all the transactions since zero state are iterated.- `end_time` – the upper time range. Only transactions with `now`less then this value are iterated. If this parameter is omitted then there isno upper time edge, so iterator never finishes.- `shard_filter` – workchains and shard prefixes that reduce the set of interestingaccounts. Account address conforms to the shard filter ifit belongs to the filter workchain and the first bits of address match tothe shard prefix. Only transactions with suitable account addresses are iterated.- `accounts_filter` – set of account addresses whose transactions must be iterated.Note that accounts filter can conflict with shard filter so application must combinethese filters carefully.<p>Iterated item is a JSON objects with transaction data. The minimal set of returnedfields is:```textidaccount_addrnowbalance_delta(format:DEC)bounce { bounce_type }in_message {    id    value(format:DEC)    msg_type    src}out_messages {    id    value(format:DEC)    msg_type    dst}```Application can request an additional fields in the `result` parameter.<p>Another parameter that affects on the returned fields is the `include_transfers`.When this parameter is `true` the iterator computes and adds `transfer` field containinglist of the useful `TransactionTransfer` objects.Each transfer is calculated from the particular message related to the transactionand has the following structure:- message – source message identifier.- isBounced – indicates that the transaction is bounced, which means the value will be returned back to the sender.- isDeposit – indicates that this transfer is the deposit (true) or withdraw (false).- counterparty – account address of the transfer source or destination depending on `isDeposit`.- value – amount of nano tokens transferred. The value is represented as a decimal stringbecause the actual value can be more precise than the JSON number can represent. Applicationmust use this string carefully – conversion to number can follow to loose of precision.<p>Application should call the `remove_iterator` when iterator is no longer required.
    * @param startTime Starting time to iterate from. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &gt;= `start_time`.Otherwise the iteration starts from zero state.<p>Must be specified in seconds.
    * @param endTime Optional end time to iterate for. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &lt; `end_time`.Otherwise the iteration never stops.<p>Must be specified in seconds.
    * @param shardFilter Shard prefix filters. If the application specifies this parameter and it is not an empty arraythen the iteration will include items related to accounts that belongs tothe specified shard prefixes.Shard prefix must be represented as a string "workchain:prefix".Where `workchain` is a signed integer and the `prefix` if a hexadecimalrepresentation if the 64-bit unsigned integer with tagged shard prefix.For example: "0:3800000000000000".Account address conforms to the shard filter ifit belongs to the filter workchain and the first bits of address match tothe shard prefix. Only transactions with suitable account addresses are iterated.
    * @param accountsFilter Account address filter. Application can specify the list of accounts for whichit wants to iterate transactions.<p>If this parameter is missing or an empty list then the library iteratestransactions for all accounts that pass the shard filter.<p>Note that the library doesn't detect conflicts between the account filter and the shard filterif both are specified.So it is an application responsibility to specify the correct filter combination.
    * @param result Projection (result) string. List of the fields that must be returned for iterated items.This field is the same as the `result` parameter ofthe `query_collection` function.Note that iterated items can contain additional fields that arenot requested in the `result`.
    * @param includeTransfers Include `transfers` field in iterated transactions. If this parameter is `true` then each transaction contains field`transfers` with list of transfer. See more about this structure in function description.
    * @return {@link tech.deplant.java4ever.binding.Net.RegisteredIterator}
    */
    public static RegisteredIterator createTransactionIterator(@NonNull Context ctx,  Number startTime,  Number endTime,  String[] shardFilter,  String[] accountsFilter,  String result,  Boolean includeTransfers)  throws JsonProcessingException {
        return ctx.call("net.create_transaction_iterator", new ParamsOfCreateTransactionIterator(startTime, endTime, shardFilter, accountsFilter, result, includeTransfers), RegisteredIterator.class);
    }

    /**
    * <h2>net.resume_transaction_iterator</h2>
    * Resumes transaction iterator. The iterator stays exactly at the same position where the `resume_state` was caught.Note that `resume_state` doesn't store the account filter. If the application requiresto use the same account filter as it was when the iterator was created then the applicationmust pass the account filter again in `accounts_filter` parameter.<p>Application should call the `remove_iterator` when iterator is no longer required.
    * @param resumeState Iterator state from which to resume. Same as value returned from `iterator_next`.
    * @param accountsFilter Account address filter. Application can specify the list of accounts for whichit wants to iterate transactions.<p>If this parameter is missing or an empty list then the library iteratestransactions for all accounts that passes the shard filter.<p>Note that the library doesn't detect conflicts between the account filter and the shard filterif both are specified.So it is the application's responsibility to specify the correct filter combination.
    * @return {@link tech.deplant.java4ever.binding.Net.RegisteredIterator}
    */
    public static RegisteredIterator resumeTransactionIterator(@NonNull Context ctx, @NonNull Map<String,Object> resumeState,  String[] accountsFilter)  throws JsonProcessingException {
        return ctx.call("net.resume_transaction_iterator", new ParamsOfResumeTransactionIterator(resumeState, accountsFilter), RegisteredIterator.class);
    }

    /**
    * <h2>net.iterator_next</h2>
    * Returns next available items. In addition to available items this function returns the `has_more` flagindicating that the iterator isn't reach the end of the iterated range yet.<p>This function can return the empty list of available items butindicates that there are more items is available.This situation appears when the iterator doesn't reach iterated rangebut database doesn't contains available items yet.<p>If application requests resume state in `return_resume_state` parameterthen this function returns `resume_state` that can be used later toresume the iteration from the position after returned items.<p>The structure of the items returned depends on the iterator used.See the description to the appropriated iterator creation function.
    * @param iterator Iterator handle 
    * @param limit Maximum count of the returned items. If value is missing or is less than 1 the library uses 1.
    * @param returnResumeState Indicates that function must return the iterator state that can be used for resuming iteration. 
    * @return {@link tech.deplant.java4ever.binding.Net.ResultOfIteratorNext}
    */
    public static ResultOfIteratorNext iteratorNext(@NonNull Context ctx, @NonNull Number iterator,  Number limit,  Boolean returnResumeState)  throws JsonProcessingException {
        return ctx.call("net.iterator_next", new ParamsOfIteratorNext(iterator, limit, returnResumeState), ResultOfIteratorNext.class);
    }

    /**
    * <h2>net.remove_iterator</h2>
    * Removes an iterator Frees all resources allocated in library to serve iterator.<p>Application always should call the `remove_iterator` when iteratoris no longer required.
    * @param handle Iterator handle. Must be removed using `remove_iterator`when it is no more needed for the application.
    * @return {@link tech.deplant.java4ever.binding.Net.Void}
    */
    public static Void removeIterator(@NonNull Context ctx, @NonNull Number handle)  throws JsonProcessingException {
        return ctx.call("net.remove_iterator", new RegisteredIterator(handle), Void.class);
    }

}
