package tech.deplant.java4ever.binding;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 *
 */
public class Net {


    /**
     * Performs DAppServer GraphQL query.
     *
     * @param query     GraphQL query text.
     * @param variables Variables used in query. Must be a map with named values that can be used in query.
     */
    public static CompletableFuture<ResultOfQuery> query(@NonNull Context context, @NonNull String query, Map<String, Object> variables) {
        return context.future("net.query", new ParamsOfQuery(query, variables), ResultOfQuery.class);
    }

    /**
     * Performs multiple queries per single fetch.
     *
     * @param operations List of query operations that must be performed per single fetch.
     */
    public static CompletableFuture<ResultOfBatchQuery> batchQuery(@NonNull Context context, @NonNull ParamsOfQueryOperation[] operations) {
        return context.future("net.batch_query", new ParamsOfBatchQuery(operations), ResultOfBatchQuery.class);
    }

    /**
     * Queries collection data
     *
     * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
     * @param filter     Collection filter
     * @param result     Projection (result) string
     * @param order      Sorting order
     * @param limit      Number of documents to return
     */
    public static CompletableFuture<ResultOfQueryCollection> queryCollection(@NonNull Context context, @NonNull String collection, Map<String, Object> filter, @NonNull String result, OrderBy[] order, Number limit) {
        return context.future("net.query_collection", new ParamsOfQueryCollection(collection, filter, result, order, limit), ResultOfQueryCollection.class);
    }

    /**
     * Aggregates collection data.
     *
     * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
     * @param filter     Collection filter
     * @param fields     Projection (result) string
     */
    public static CompletableFuture<ResultOfAggregateCollection> aggregateCollection(@NonNull Context context, @NonNull String collection, Map<String, Object> filter, FieldAggregation[] fields) {
        return context.future("net.aggregate_collection", new ParamsOfAggregateCollection(collection, filter, fields), ResultOfAggregateCollection.class);
    }

    /**
     * Returns an object that fulfills the conditions or waits for its appearance
     *
     * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
     * @param filter     Collection filter
     * @param result     Projection (result) string
     * @param timeout    Query timeout
     */
    public static CompletableFuture<ResultOfWaitForCollection> waitForCollection(@NonNull Context context, @NonNull String collection, Map<String, Object> filter, @NonNull String result, Number timeout) {
        return context.future("net.wait_for_collection", new ParamsOfWaitForCollection(collection, filter, result, timeout), ResultOfWaitForCollection.class);
    }

    /**
     * Cancels a subscription
     *
     * @param handle Subscription handle. Must be closed with `unsubscribe`
     */
    public static CompletableFuture<Void> unsubscribe(@NonNull Context context, @NonNull Number handle) {
        return context.future("net.unsubscribe", new ResultOfSubscribeCollection(handle), Void.class);
    }

    /**
     * Creates a subscription
     *
     * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
     * @param filter     Collection filter
     * @param result     Projection (result) string
     */
    public static CompletableFuture<ResultOfSubscribeCollection> subscribeCollection(@NonNull Context context, @NonNull String collection, Map<String, Object> filter, @NonNull String result, Consumer<SubscribeCollectionEvent> consumer) {
        return Context.futureCallback("net.subscribe_collection", context, new ParamsOfSubscribeCollection(collection, filter, result), ResultOfSubscribeCollection.class);
    }

    /**
     * Suspends network module to stop any network activity
     */
    public static CompletableFuture<Void> suspend(@NonNull Context context) {
        return context.future("net.suspend", null, Void.class);
    }

    /**
     * Resumes network module to enable network activity
     */
    public static CompletableFuture<Void> resume(@NonNull Context context) {
        return context.future("net.resume", null, Void.class);
    }

    /**
     * Returns ID of the last block in a specified account shard
     *
     * @param address Account address
     */
    public static CompletableFuture<ResultOfFindLastShardBlock> findLastShardBlock(@NonNull Context context, @NonNull String address) {
        return context.future("net.find_last_shard_block", new ParamsOfFindLastShardBlock(address), ResultOfFindLastShardBlock.class);
    }

    /**
     * Requests the list of alternative endpoints from server
     */
    public static CompletableFuture<EndpointsSet> fetchEndpoints(@NonNull Context context) {
        return context.future("net.fetch_endpoints", null, EndpointsSet.class);
    }

    /**
     * Sets the list of endpoints to use on reinit
     *
     * @param endpoints List of endpoints provided by server
     */
    public static CompletableFuture<Void> setEndpoints(@NonNull Context context, @NonNull String[] endpoints) {
        return context.future("net.set_endpoints", new EndpointsSet(endpoints), Void.class);
    }

    /**
     * Requests the list of alternative endpoints from server
     */
    public static CompletableFuture<ResultOfGetEndpoints> getEndpoints(@NonNull Context context) {
        return context.future("net.get_endpoints", null, ResultOfGetEndpoints.class);
    }

    /**
     * Allows to query and paginate through the list of accounts that the specified account has interacted with, sorted by the time of the last internal message between accounts
     *
     * @param account Account address
     * @param result  Projection (result) string
     * @param first   Number of counterparties to return
     * @param after   `cursor` field of the last received result
     */
    public static CompletableFuture<ResultOfQueryCollection> queryCounterparties(@NonNull Context context, @NonNull String account, @NonNull String result, Number first, String after) {
        return context.future("net.query_counterparties", new ParamsOfQueryCounterparties(account, result, first, after), ResultOfQueryCollection.class);
    }

    /**
     * Returns a tree of transactions triggered by a specific message.
     *
     * @param inMsg       Input message id.
     * @param abiRegistry List of contract ABIs that will be used to decode message bodies. Library will try to decode each returned message body using any ABI from the registry.
     * @param timeout     Timeout used to limit waiting time for the missing messages and transaction. If some of the following messages and transactions are missing yetThe maximum waiting time is regulated by this option.<p>Default value is 60000 (1 min).
     */
    public static CompletableFuture<ResultOfQueryTransactionTree> queryTransactionTree(@NonNull Context context, @NonNull String inMsg, Abi.ABI[] abiRegistry, Number timeout) {
        return context.future("net.query_transaction_tree", new ParamsOfQueryTransactionTree(inMsg, abiRegistry, timeout), ResultOfQueryTransactionTree.class);
    }

    /**
     * Creates block iterator.
     *
     * @param startTime   Starting time to iterate from. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &gt;= `start_time`.Otherwise the iteration starts from zero state.<p>Must be specified in seconds.
     * @param endTime     Optional end time to iterate for. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &lt; `end_time`.Otherwise the iteration never stops.<p>Must be specified in seconds.
     * @param shardFilter Shard prefix filter. If the application specifies this parameter and it is not the empty arraythen the iteration will include items related to accounts that belongs tothe specified shard prefixes.Shard prefix must be represented as a string "workchain:prefix".Where `workchain` is a signed integer and the `prefix` if a hexadecimalrepresentation if the 64-bit unsigned integer with tagged shard prefix.For example: "0:3800000000000000".
     * @param result      Projection (result) string. List of the fields that must be returned for iterated items.This field is the same as the `result` parameter ofthe `query_collection` function.Note that iterated items can contains additional fields that arenot requested in the `result`.
     */
    public static CompletableFuture<RegisteredIterator> createBlockIterator(@NonNull Context context, Number startTime, Number endTime, String[] shardFilter, String result) {
        return context.future("net.create_block_iterator", new ParamsOfCreateBlockIterator(startTime, endTime, shardFilter, result), RegisteredIterator.class);
    }

    /**
     * Resumes block iterator.
     *
     * @param resumeState Iterator state from which to resume. Same as value returned from `iterator_next`.
     */
    public static CompletableFuture<RegisteredIterator> resumeBlockIterator(@NonNull Context context, @NonNull Map<String, Object> resumeState) {
        return context.future("net.resume_block_iterator", new ParamsOfResumeBlockIterator(resumeState), RegisteredIterator.class);
    }

    /**
     * Creates transaction iterator.
     *
     * @param startTime        Starting time to iterate from. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &gt;= `start_time`.Otherwise the iteration starts from zero state.<p>Must be specified in seconds.
     * @param endTime          Optional end time to iterate for. If the application specifies this parameter then the iterationincludes blocks with `gen_utime` &lt; `end_time`.Otherwise the iteration never stops.<p>Must be specified in seconds.
     * @param shardFilter      Shard prefix filters. If the application specifies this parameter and it is not an empty arraythen the iteration will include items related to accounts that belongs tothe specified shard prefixes.Shard prefix must be represented as a string "workchain:prefix".Where `workchain` is a signed integer and the `prefix` if a hexadecimalrepresentation if the 64-bit unsigned integer with tagged shard prefix.For example: "0:3800000000000000".Account address conforms to the shard filter ifit belongs to the filter workchain and the first bits of address match tothe shard prefix. Only transactions with suitable account addresses are iterated.
     * @param accountsFilter   Account address filter. Application can specify the list of accounts for whichit wants to iterate transactions.<p>If this parameter is missing or an empty list then the library iteratestransactions for all accounts that pass the shard filter.<p>Note that the library doesn't detect conflicts between the account filter and the shard filterif both are specified.So it is an application responsibility to specify the correct filter combination.
     * @param result           Projection (result) string. List of the fields that must be returned for iterated items.This field is the same as the `result` parameter ofthe `query_collection` function.Note that iterated items can contain additional fields that arenot requested in the `result`.
     * @param includeTransfers Include `transfers` field in iterated transactions. If this parameter is `true` then each transaction contains field`transfers` with list of transfer. See more about this structure in function description.
     */
    public static CompletableFuture<RegisteredIterator> createTransactionIterator(@NonNull Context context, Number startTime, Number endTime, String[] shardFilter, String[] accountsFilter, String result, Boolean includeTransfers) {
        return context.future("net.create_transaction_iterator", new ParamsOfCreateTransactionIterator(startTime, endTime, shardFilter, accountsFilter, result, includeTransfers), RegisteredIterator.class);
    }

    /**
     * Resumes transaction iterator.
     *
     * @param resumeState    Iterator state from which to resume. Same as value returned from `iterator_next`.
     * @param accountsFilter Account address filter. Application can specify the list of accounts for whichit wants to iterate transactions.<p>If this parameter is missing or an empty list then the library iteratestransactions for all accounts that passes the shard filter.<p>Note that the library doesn't detect conflicts between the account filter and the shard filterif both are specified.So it is the application's responsibility to specify the correct filter combination.
     */
    public static CompletableFuture<RegisteredIterator> resumeTransactionIterator(@NonNull Context context, @NonNull Map<String, Object> resumeState, String[] accountsFilter) {
        return context.future("net.resume_transaction_iterator", new ParamsOfResumeTransactionIterator(resumeState, accountsFilter), RegisteredIterator.class);
    }

    /**
     * Returns next available items.
     *
     * @param iterator          Iterator handle
     * @param limit             Maximum count of the returned items. If value is missing or is less than 1 the library uses 1.
     * @param returnResumeState Indicates that function must return the iterator state that can be used for resuming iteration.
     */
    public static CompletableFuture<ResultOfIteratorNext> iteratorNext(@NonNull Context context, @NonNull Number iterator, Number limit, Boolean returnResumeState) {
        return context.future("net.iterator_next", new ParamsOfIteratorNext(iterator, limit, returnResumeState), ResultOfIteratorNext.class);
    }

    /**
     * Removes an iterator
     *
     * @param handle Iterator handle. Must be removed using `remove_iterator`when it is no more needed for the application.
     */
    public static CompletableFuture<Void> removeIterator(@NonNull Context context, @NonNull Number handle) {
        return context.future("net.remove_iterator", new RegisteredIterator(handle), Void.class);
    }

    /**
     *
     */
    public enum SortDirection {

        /**
         *
         */
        ASC,

        /**
         *
         */
        DESC
    }

    /**
     *
     */
    public enum AggregationFn {

        /**
         *
         */
        COUNT,

        /**
         *
         */
        MIN,

        /**
         *
         */
        MAX,

        /**
         *
         */
        SUM,

        /**
         *
         */
        AVERAGE
    }

    @Value
    public static class OrderBy extends JsonData {
        @SerializedName("path")
        @NonNull String path;
        @SerializedName("direction")
        @NonNull SortDirection direction;

    }

    public static abstract class ParamsOfQueryOperation {


        @Value
        public static class QueryCollection extends ParamsOfQueryOperation {

            /**
             * Collection name (accounts, blocks, transactions, messages, block_signatures)
             */
            @SerializedName("collection")
            @NonNull String collection;
            @SerializedName("filter")
            @Getter(AccessLevel.NONE)
            Map<String, Object> filter;
            /**
             * Projection (result) string
             */
            @SerializedName("result")
            @NonNull String result;
            @SerializedName("order")
            @Getter(AccessLevel.NONE)
            OrderBy[] order;
            @SerializedName("limit")
            @Getter(AccessLevel.NONE)
            Number limit;

            /**
             * Collection filter
             */
            public Optional<Map<String, Object>> filter() {
                return Optional.ofNullable(this.filter);
            }

            /**
             * Sorting order
             */
            public Optional<OrderBy[]> order() {
                return Optional.ofNullable(this.order);
            }

            /**
             * Number of documents to return
             */
            public Optional<Number> limit() {
                return Optional.ofNullable(this.limit);
            }

        }


        @Value
        public static class WaitForCollection extends ParamsOfQueryOperation {

            /**
             * Collection name (accounts, blocks, transactions, messages, block_signatures)
             */
            @SerializedName("collection")
            @NonNull String collection;
            @SerializedName("filter")
            @Getter(AccessLevel.NONE)
            Map<String, Object> filter;
            /**
             * Projection (result) string
             */
            @SerializedName("result")
            @NonNull String result;
            @SerializedName("timeout")
            @Getter(AccessLevel.NONE)
            Number timeout;

            /**
             * Collection filter
             */
            public Optional<Map<String, Object>> filter() {
                return Optional.ofNullable(this.filter);
            }

            /**
             * Query timeout
             */
            public Optional<Number> timeout() {
                return Optional.ofNullable(this.timeout);
            }

        }


        @Value
        public static class AggregateCollection extends ParamsOfQueryOperation {

            /**
             * Collection name (accounts, blocks, transactions, messages, block_signatures)
             */
            @SerializedName("collection")
            @NonNull String collection;
            @SerializedName("filter")
            @Getter(AccessLevel.NONE)
            Map<String, Object> filter;
            @SerializedName("fields")
            @Getter(AccessLevel.NONE)
            FieldAggregation[] fields;

            /**
             * Collection filter
             */
            public Optional<Map<String, Object>> filter() {
                return Optional.ofNullable(this.filter);
            }

            /**
             * Projection (result) string
             */
            public Optional<FieldAggregation[]> fields() {
                return Optional.ofNullable(this.fields);
            }

        }


        @Value
        public static class QueryCounterparties extends ParamsOfQueryOperation {

            /**
             * Account address
             */
            @SerializedName("account")
            @NonNull String account;

            /**
             * Projection (result) string
             */
            @SerializedName("result")
            @NonNull String result;
            @SerializedName("first")
            @Getter(AccessLevel.NONE)
            Number first;
            @SerializedName("after")
            @Getter(AccessLevel.NONE)
            String after;

            /**
             * Number of counterparties to return
             */
            public Optional<Number> first() {
                return Optional.ofNullable(this.first);
            }

            /**
             * `cursor` field of the last received result
             */
            public Optional<String> after() {
                return Optional.ofNullable(this.after);
            }

        }
    }

    @Value
    public static class FieldAggregation extends JsonData {

        /**
         * Dot separated path to the field
         */
        @SerializedName("field")
        @NonNull String field;

        /**
         * Aggregation function that must be applied to field values
         */
        @SerializedName("fn")
        @NonNull AggregationFn fn;

    }

    @Value
    public static class TransactionNode extends JsonData {

        /**
         * Transaction id.
         */
        @SerializedName("id")
        @NonNull String id;

        /**
         * In message id.
         */
        @SerializedName("in_msg")
        @NonNull String inMsg;

        /**
         * Out message ids.
         */
        @SerializedName("out_msgs")
        @NonNull String[] outMsgs;

        /**
         * Account address.
         */
        @SerializedName("account_addr")
        @NonNull String accountAddr;

        /**
         * Transactions total fees.
         */
        @SerializedName("total_fees")
        @NonNull String totalFees;

        /**
         * Aborted flag.
         */
        @SerializedName("aborted")
        @NonNull Boolean aborted;
        @SerializedName("exit_code")
        @Getter(AccessLevel.NONE)
        Number exitCode;

        /**
         * Compute phase exit code.
         */
        public Optional<Number> exitCode() {
            return Optional.ofNullable(this.exitCode);
        }

    }

    @Value
    public static class MessageNode extends JsonData {

        /**
         * Message id.
         */
        @SerializedName("id")
        @NonNull String id;
        @SerializedName("src_transaction_id")
        @Getter(AccessLevel.NONE)
        String srcTransactionId;
        @SerializedName("dst_transaction_id")
        @Getter(AccessLevel.NONE)
        String dstTransactionId;
        @SerializedName("src")
        @Getter(AccessLevel.NONE)
        String src;
        @SerializedName("dst")
        @Getter(AccessLevel.NONE)
        String dst;
        @SerializedName("value")
        @Getter(AccessLevel.NONE)
        String value;
        /**
         * Bounce flag.
         */
        @SerializedName("bounce")
        @NonNull Boolean bounce;
        @SerializedName("decoded_body")
        @Getter(AccessLevel.NONE)
        Abi.DecodedMessageBody decodedBody;

        /**
         * Source transaction id.
         */
        public Optional<String> srcTransactionId() {
            return Optional.ofNullable(this.srcTransactionId);
        }

        /**
         * Destination transaction id.
         */
        public Optional<String> dstTransactionId() {
            return Optional.ofNullable(this.dstTransactionId);
        }

        /**
         * Source address.
         */
        public Optional<String> src() {
            return Optional.ofNullable(this.src);
        }

        /**
         * Destination address.
         */
        public Optional<String> dst() {
            return Optional.ofNullable(this.dst);
        }

        /**
         * Transferred tokens value.
         */
        public Optional<String> value() {
            return Optional.ofNullable(this.value);
        }

        /**
         * Decoded body.
         */
        public Optional<Abi.DecodedMessageBody> decodedBody() {
            return Optional.ofNullable(this.decodedBody);
        }

    }

    @Value
    public static class ParamsOfQuery extends JsonData {

        /**
         * GraphQL query text.
         */
        @SerializedName("query")
        @NonNull String query;
        @SerializedName("variables")
        @Getter(AccessLevel.NONE)
        Map<String, Object> variables;

        /**
         * Variables used in query.
         */
        public Optional<Map<String, Object>> variables() {
            return Optional.ofNullable(this.variables);
        }

    }

    @Value
    public static class ResultOfQuery extends JsonData {

        /**
         * Result provided by DAppServer.
         */
        @SerializedName("result")
        @NonNull Map<String, Object> result;

    }

    @Value
    public static class ParamsOfBatchQuery extends JsonData {

        /**
         * List of query operations that must be performed per single fetch.
         */
        @SerializedName("operations")
        @NonNull ParamsOfQueryOperation[] operations;

    }

    @Value
    public static class ResultOfBatchQuery extends JsonData {

        /**
         * Result values for batched queries.
         */
        @SerializedName("results")
        @NonNull Map<String, Object>[] results;

    }

    @Value
    public static class ParamsOfQueryCollection extends JsonData {

        /**
         * Collection name (accounts, blocks, transactions, messages, block_signatures)
         */
        @SerializedName("collection")
        @NonNull String collection;
        @SerializedName("filter")
        @Getter(AccessLevel.NONE)
        Map<String, Object> filter;
        /**
         * Projection (result) string
         */
        @SerializedName("result")
        @NonNull String result;
        @SerializedName("order")
        @Getter(AccessLevel.NONE)
        OrderBy[] order;
        @SerializedName("limit")
        @Getter(AccessLevel.NONE)
        Number limit;

        /**
         * Collection filter
         */
        public Optional<Map<String, Object>> filter() {
            return Optional.ofNullable(this.filter);
        }

        /**
         * Sorting order
         */
        public Optional<OrderBy[]> order() {
            return Optional.ofNullable(this.order);
        }

        /**
         * Number of documents to return
         */
        public Optional<Number> limit() {
            return Optional.ofNullable(this.limit);
        }

    }

    @Value
    public static class ResultOfQueryCollection extends JsonData {

        /**
         * Objects that match the provided criteria
         */
        @SerializedName("result")
        @NonNull Map<String, Object>[] result;

    }

    @Value
    public static class ParamsOfAggregateCollection extends JsonData {

        /**
         * Collection name (accounts, blocks, transactions, messages, block_signatures)
         */
        @SerializedName("collection")
        @NonNull String collection;
        @SerializedName("filter")
        @Getter(AccessLevel.NONE)
        Map<String, Object> filter;
        @SerializedName("fields")
        @Getter(AccessLevel.NONE)
        FieldAggregation[] fields;

        /**
         * Collection filter
         */
        public Optional<Map<String, Object>> filter() {
            return Optional.ofNullable(this.filter);
        }

        /**
         * Projection (result) string
         */
        public Optional<FieldAggregation[]> fields() {
            return Optional.ofNullable(this.fields);
        }

    }

    @Value
    public static class ResultOfAggregateCollection extends JsonData {

        /**
         * Values for requested fields.
         */
        @SerializedName("values")
        @NonNull Map<String, Object> values;

    }

    @Value
    public static class ParamsOfWaitForCollection extends JsonData {

        /**
         * Collection name (accounts, blocks, transactions, messages, block_signatures)
         */
        @SerializedName("collection")
        @NonNull String collection;
        @SerializedName("filter")
        @Getter(AccessLevel.NONE)
        Map<String, Object> filter;
        /**
         * Projection (result) string
         */
        @SerializedName("result")
        @NonNull String result;
        @SerializedName("timeout")
        @Getter(AccessLevel.NONE)
        Number timeout;

        /**
         * Collection filter
         */
        public Optional<Map<String, Object>> filter() {
            return Optional.ofNullable(this.filter);
        }

        /**
         * Query timeout
         */
        public Optional<Number> timeout() {
            return Optional.ofNullable(this.timeout);
        }

    }

    @Value
    public static class ResultOfWaitForCollection extends JsonData {

        /**
         * First found object that matches the provided criteria
         */
        @SerializedName("result")
        @NonNull Map<String, Object> result;

    }

    @Value
    public static class ResultOfSubscribeCollection extends JsonData {

        /**
         * Subscription handle.
         */
        @SerializedName("handle")
        @NonNull Number handle;

    }

    @Value
    public static class ParamsOfSubscribeCollection extends JsonData {

        /**
         * Collection name (accounts, blocks, transactions, messages, block_signatures)
         */
        @SerializedName("collection")
        @NonNull String collection;
        @SerializedName("filter")
        @Getter(AccessLevel.NONE)
        Map<String, Object> filter;
        /**
         * Projection (result) string
         */
        @SerializedName("result")
        @NonNull String result;

        /**
         * Collection filter
         */
        public Optional<Map<String, Object>> filter() {
            return Optional.ofNullable(this.filter);
        }

    }

    @Value
    public static class ParamsOfFindLastShardBlock extends JsonData {

        /**
         * Account address
         */
        @SerializedName("address")
        @NonNull String address;

    }

    @Value
    public static class ResultOfFindLastShardBlock extends JsonData {

        /**
         * Account shard last block ID
         */
        @SerializedName("block_id")
        @NonNull String blockId;

    }

    @Value
    public static class EndpointsSet extends JsonData {

        /**
         * List of endpoints provided by server
         */
        @SerializedName("endpoints")
        @NonNull String[] endpoints;

    }

    @Value
    public static class ResultOfGetEndpoints extends JsonData {

        /**
         * Current query endpoint
         */
        @SerializedName("query")
        @NonNull String query;

        /**
         * List of all endpoints used by client
         */
        @SerializedName("endpoints")
        @NonNull String[] endpoints;

    }

    @Value
    public static class ParamsOfQueryCounterparties extends JsonData {

        /**
         * Account address
         */
        @SerializedName("account")
        @NonNull String account;

        /**
         * Projection (result) string
         */
        @SerializedName("result")
        @NonNull String result;
        @SerializedName("first")
        @Getter(AccessLevel.NONE)
        Number first;
        @SerializedName("after")
        @Getter(AccessLevel.NONE)
        String after;

        /**
         * Number of counterparties to return
         */
        public Optional<Number> first() {
            return Optional.ofNullable(this.first);
        }

        /**
         * `cursor` field of the last received result
         */
        public Optional<String> after() {
            return Optional.ofNullable(this.after);
        }

    }

    @Value
    public static class ParamsOfQueryTransactionTree extends JsonData {

        /**
         * Input message id.
         */
        @SerializedName("in_msg")
        @NonNull String inMsg;
        @SerializedName("abi_registry")
        @Getter(AccessLevel.NONE)
        Abi.ABI[] abiRegistry;
        @SerializedName("timeout")
        @Getter(AccessLevel.NONE)
        Number timeout;

        /**
         * List of contract ABIs that will be used to decode message bodies. Library will try to decode each returned message body using any ABI from the registry.
         */
        public Optional<Abi.ABI[]> abiRegistry() {
            return Optional.ofNullable(this.abiRegistry);
        }

        /**
         * Timeout used to limit waiting time for the missing messages and transaction.
         */
        public Optional<Number> timeout() {
            return Optional.ofNullable(this.timeout);
        }

    }

    @Value
    public static class ResultOfQueryTransactionTree extends JsonData {

        /**
         * Messages.
         */
        @SerializedName("messages")
        @NonNull MessageNode[] messages;

        /**
         * Transactions.
         */
        @SerializedName("transactions")
        @NonNull TransactionNode[] transactions;

    }

    @Value
    public static class ParamsOfCreateBlockIterator extends JsonData {
        @SerializedName("start_time")
        @Getter(AccessLevel.NONE)
        Number startTime;
        @SerializedName("end_time")
        @Getter(AccessLevel.NONE)
        Number endTime;
        @SerializedName("shard_filter")
        @Getter(AccessLevel.NONE)
        String[] shardFilter;
        @SerializedName("result")
        @Getter(AccessLevel.NONE)
        String result;

        /**
         * Starting time to iterate from.
         */
        public Optional<Number> startTime() {
            return Optional.ofNullable(this.startTime);
        }

        /**
         * Optional end time to iterate for.
         */
        public Optional<Number> endTime() {
            return Optional.ofNullable(this.endTime);
        }

        /**
         * Shard prefix filter.
         */
        public Optional<String[]> shardFilter() {
            return Optional.ofNullable(this.shardFilter);
        }

        /**
         * Projection (result) string.
         */
        public Optional<String> result() {
            return Optional.ofNullable(this.result);
        }

    }

    @Value
    public static class RegisteredIterator extends JsonData {

        /**
         * Iterator handle.
         */
        @SerializedName("handle")
        @NonNull Number handle;

    }

    @Value
    public static class ParamsOfResumeBlockIterator extends JsonData {

        /**
         * Iterator state from which to resume.
         */
        @SerializedName("resume_state")
        @NonNull Map<String, Object> resumeState;

    }

    @Value
    public static class ParamsOfCreateTransactionIterator extends JsonData {
        @SerializedName("start_time")
        @Getter(AccessLevel.NONE)
        Number startTime;
        @SerializedName("end_time")
        @Getter(AccessLevel.NONE)
        Number endTime;
        @SerializedName("shard_filter")
        @Getter(AccessLevel.NONE)
        String[] shardFilter;
        @SerializedName("accounts_filter")
        @Getter(AccessLevel.NONE)
        String[] accountsFilter;
        @SerializedName("result")
        @Getter(AccessLevel.NONE)
        String result;
        @SerializedName("include_transfers")
        @Getter(AccessLevel.NONE)
        Boolean includeTransfers;

        /**
         * Starting time to iterate from.
         */
        public Optional<Number> startTime() {
            return Optional.ofNullable(this.startTime);
        }

        /**
         * Optional end time to iterate for.
         */
        public Optional<Number> endTime() {
            return Optional.ofNullable(this.endTime);
        }

        /**
         * Shard prefix filters.
         */
        public Optional<String[]> shardFilter() {
            return Optional.ofNullable(this.shardFilter);
        }

        /**
         * Account address filter.
         */
        public Optional<String[]> accountsFilter() {
            return Optional.ofNullable(this.accountsFilter);
        }

        /**
         * Projection (result) string.
         */
        public Optional<String> result() {
            return Optional.ofNullable(this.result);
        }

        /**
         * Include `transfers` field in iterated transactions.
         */
        public Optional<Boolean> includeTransfers() {
            return Optional.ofNullable(this.includeTransfers);
        }

    }

    @Value
    public static class ParamsOfResumeTransactionIterator extends JsonData {

        /**
         * Iterator state from which to resume.
         */
        @SerializedName("resume_state")
        @NonNull Map<String, Object> resumeState;
        @SerializedName("accounts_filter")
        @Getter(AccessLevel.NONE)
        String[] accountsFilter;

        /**
         * Account address filter.
         */
        public Optional<String[]> accountsFilter() {
            return Optional.ofNullable(this.accountsFilter);
        }

    }

    @Value
    public static class ParamsOfIteratorNext extends JsonData {

        /**
         * Iterator handle
         */
        @SerializedName("iterator")
        @NonNull Number iterator;
        @SerializedName("limit")
        @Getter(AccessLevel.NONE)
        Number limit;
        @SerializedName("return_resume_state")
        @Getter(AccessLevel.NONE)
        Boolean returnResumeState;

        /**
         * Maximum count of the returned items.
         */
        public Optional<Number> limit() {
            return Optional.ofNullable(this.limit);
        }

        /**
         * Indicates that function must return the iterator state that can be used for resuming iteration.
         */
        public Optional<Boolean> returnResumeState() {
            return Optional.ofNullable(this.returnResumeState);
        }

    }

    @Value
    public static class ResultOfIteratorNext extends JsonData {

        /**
         * Next available items.
         */
        @SerializedName("items")
        @NonNull Map<String, Object>[] items;

        /**
         * Indicates that there are more available items in iterated range.
         */
        @SerializedName("has_more")
        @NonNull Boolean hasMore;
        @SerializedName("resume_state")
        @Getter(AccessLevel.NONE)
        Map<String, Object> resumeState;

        /**
         * Optional iterator state that can be used for resuming iteration.
         */
        public Optional<Map<String, Object>> resumeState() {
            return Optional.ofNullable(this.resumeState);
        }

    }

}
