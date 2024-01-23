package tech.deplant.java4ever.unit;

import com.yegor256.OnlineMeans;
import com.yegor256.WeAreOnline;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.commons.regex.Special;
import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.EverSdk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WeAreOnline.class)
public class CryptoTests {

	private static final Logger log = LoggerFactory.getLogger(CryptoTests.class);

	@BeforeAll
	public static void loadSdk() {
		EverSdk.load();
	}

	@ParameterizedTest
	@ValueSource(ints = {12,24})
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void mnemonic_from_random_should_pass_regexp_for_word_count(int words) throws EverSdkException {
		int ctxId = TestEnv.newContext();
		Pattern pattern = Pattern.compile("[\\w-]+");
		Matcher matcher = pattern.matcher(Crypto.mnemonicFromRandom(ctxId, Crypto.MnemonicDictionary.English, words).phrase());
		int count = 0;
		while (matcher.find())
			count++;
		assertEquals(words, count);
	}

}
