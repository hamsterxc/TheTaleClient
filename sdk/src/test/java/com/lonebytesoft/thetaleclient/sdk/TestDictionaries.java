package com.lonebytesoft.thetaleclient.sdk;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Action;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactEffect;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CompanionFeature;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CompanionFeatureType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CompanionSpecies;
import com.lonebytesoft.thetaleclient.sdk.dictionary.EquipmentType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.GameState;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Gender;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Habit;
import com.lonebytesoft.thetaleclient.sdk.dictionary.HeroAction;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Profession;
import com.lonebytesoft.thetaleclient.sdk.dictionary.QuestActorType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ThirdPartyAuthState;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Pair;

/**
 * @author Hamster
 * @since 25.03.2015
 *
 * Tests for API dictionaries
 * Are not tested:
 * - Archetype {@see http://the-tale.org/guide/mobs/}
 * - CompanionDedication {@see http://the-tale.org/guide/companions/}
 * - CompanionFeatureType {@see http://the-tale.org/guide/companions/}
 * - CompanionRarity {@see http://the-tale.org/guide/companions/}
 * - CompanionType {@see http://the-tale.org/guide/companions/}
 * - HeroMode {@see http://the-tale.org/guide/api#game_info}
 * - PvpAbility {@see http://the-tale.org/guide/api#game_info}
 * - QuestType
 * - RatingItem {@see http://the-tale.org/guide/api#account_info}
 */
public class TestDictionaries extends TestCase {

    private Document apiGuide;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        apiGuide = getDocument("http://the-tale.org/guide/api", "Could not get API guide");
    }

    public void testAction() {
        final List<List<String>> table = getApiTableData(9);
        checkSize(Action.class, table.size());

        for(final List<String> item : table) {
            final String code = item.get(0);
            final Action action = ObjectUtils.getEnumForCode(Action.class, code);
            assertNotNull(
                    String.format("Action not found: %s", code),
                    action);

            final String name = item.get(1);
            assertEquals(
                    String.format("Action incorrect name: %s", name),
                    name, action.name);
        }
    }

    public void testArtifactEffect() {
        final Document document = getDocument("http://the-tale.org/guide/artifacts/", "Could not get artifacts guide page");
        final List<List<String>> tableDescriptions = getTableData(document.select("div#pgf-effects table").first(), true, 0, 1);
        checkSize(ArtifactEffect.class, tableDescriptions.size() + 1, "name-description"); // "no effect" is not in this list
        for(final List<String> item : tableDescriptions) {
            final String name = item.get(0);
            final ArtifactEffect artifactEffect = ObjectUtils.getEnumForName(ArtifactEffect.class, name);
            assertNotNull(
                    String.format("ArtifactEffect not found: name = %s", name),
                    artifactEffect);

            final String description = item.get(1);
            assertEquals(
                    String.format("ArtifactEffect incorrect description: %s", description),
                    description, artifactEffect.description);
        }

        final List<List<String>> tableNames = getApiTableData(7);
        checkSize(ArtifactEffect.class, tableNames.size(), "code-name");
        for(final List<String> item : tableNames) {
            final int code = Integer.parseInt(item.get(0));
            final ArtifactEffect artifactEffect = ObjectUtils.getEnumForCode(ArtifactEffect.class, code);
            assertNotNull(
                    String.format("ArtifactEffect not found: code = %d", code),
                    artifactEffect);

            final String name = item.get(1);
            assertEquals(
                    String.format("ArtifactEffect incorrect name: %s", name),
                    name, artifactEffect.name);
        }
    }

    public void testArtifactRarity() {
        final List<List<String>> table = getApiTableData(3);
        checkSize(ArtifactRarity.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final ArtifactRarity artifactRarity = ObjectUtils.getEnumForCode(ArtifactRarity.class, code);
            assertNotNull(
                    String.format("ArtifactRarity not found: code = %d", code),
                    artifactRarity);

            final String name = item.get(1);
            assertEquals(
                    String.format("ArtifactRarity incorrect name: %s", name),
                    name, artifactRarity.name);
        }
    }

    public void testArtifactType() {
        final List<List<String>> table = getApiTableData(5);
        checkSize(ArtifactType.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final ArtifactType artifactType = ObjectUtils.getEnumForCode(ArtifactType.class, code);
            assertNotNull(
                    String.format("ArtifactType not found: code = %d", code),
                    artifactType);

            final String name = item.get(1);
            assertEquals(
                    String.format("ArtifactType incorrect name: %s", name),
                    name, artifactType.name);
        }
    }

    public void testCardRarity() {
        final List<List<String>> table = getApiTableData(4);
        checkSize(CardRarity.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final CardRarity cardRarity = ObjectUtils.getEnumForCode(CardRarity.class, code);
            assertNotNull(
                    String.format("CardRarity not found: code = %d", code),
                    cardRarity);

            final String name = item.get(1);
            assertEquals(
                    String.format("CardRarity incorrect name: %s", name),
                    name, cardRarity.name);
        }
    }

    public void testCardType() {
        final Document document = getDocument("http://the-tale.org/guide/cards/", "Could not get cards guide page");
        final List<List<String>> tableDescriptions = getTableData(document.select("table").first(), true, 1, 2);
        checkSize(CardType.class, tableDescriptions.size(), "name-description");
        for(final List<String> item : tableDescriptions) {
            final String name = item.get(0);
            final CardType cardType = ObjectUtils.getEnumForName(CardType.class, name);
            assertNotNull(
                    String.format("CardType not found: name = %s", name),
                    cardType);

            final String description = item.get(1);
            assertEquals(
                    String.format("CardType incorrect description: %s", description),
                    description, cardType.description);
        }

        final List<List<String>> table = getApiTableData(13);
        checkSize(CardType.class, table.size(), "code-name");
        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final CardType cardType = ObjectUtils.getEnumForCode(CardType.class, code);
            assertNotNull(
                    String.format("CardType not found: code = %d", code),
                    cardType);

            final String name = item.get(1);
            assertEquals(
                    String.format("CardType incorrect name: %s", name),
                    name, cardType.name);
        }
    }

    public void testCompanionFeature() {
        final Document document = getDocument("http://the-tale.org/guide/companions/", "Could not get companions guide page");
        final List<List<String>> table = getTableData(document.select("div#pgf-companion-effects-accordion table").first(), true, 0, 1, 2);
        checkSize(CompanionFeature.class, table.size());
        for(final List<String> item : table) {
            final String name = item.get(0);
            final CompanionFeature companionFeature = ObjectUtils.getEnumForName(CompanionFeature.class, name);
            assertNotNull(String.format("CompanionFeature not found: name = %s", name), companionFeature);

            final String type = item.get(1);
            assertNotNull(String.format("CompanionFeatureType not found: name = %s", type),
                    ObjectUtils.getEnumForName(CompanionFeatureType.class, type));

            final String description = item.get(2);
            assertEquals(String.format("CompanionFeature incorrect description: %s", description),
                    description, companionFeature.description);
        }
    }

    public void testCompanionSpecies() {
        final Document documentList = getDocument("http://the-tale.org/guide/companions/", "Could not get companions guide page");
        final Elements elementsList = documentList.select("table.table td a");
        checkSize(CompanionSpecies.class, elementsList.size() / 2); // each companion row has 2 links

        final Pattern patternFeatures = Pattern.compile("[^\\d]*(\\d+):\\s*(.+)");
        for(final Element elementList : elementsList) {
            final String url = elementList.attr("href");
            final int id;
            try {
                id = Integer.parseInt(url.substring(url.lastIndexOf('/') + 1));
            } catch (NumberFormatException ignored) {
                continue;
            }

            final CompanionSpecies companionSpecies = ObjectUtils.getEnumForCode(CompanionSpecies.class, id);
            assertNotNull(String.format("CompanionSpecies not found: id = %d", id), companionSpecies);

            final Document document = getDocument(
                    String.format("http://the-tale.org/guide/companions/%d/info", id),
                    String.format("Could not get companion page: id = %d", id));
            final Elements elements = document.select("table td");

            final String name = document.select(".dialog-title").first().text();
            assertEquals(String.format("CompanionSpecies id = %d incorrect name: %s", id, name),
                    name, companionSpecies.name);

            final String rarity = elements.get(0).text();
            assertEquals(String.format("CompanionSpecies %s incorrect rarity: %s", name, rarity),
                    rarity, companionSpecies.rarity.name);

            final String type = elements.get(1).text();
            assertEquals(String.format("CompanionSpecies %s incorrect type: %s", name, type),
                    type, companionSpecies.type.name);

            final String archetype = elements.get(2).text();
            assertEquals(String.format("CompanionSpecies %s incorrect archetype: %s", name, archetype),
                    archetype, companionSpecies.archetype.code);

            final String dedication = elements.get(3).text();
            assertEquals(String.format("CompanionSpecies %s incorrect dedication: %s", name, dedication),
                    dedication, companionSpecies.dedication.name);

            final int health = Integer.parseInt(elements.get(4).text());
            assertEquals(String.format("CompanionSpecies %s incorrect health: %d", name, health),
                    health, companionSpecies.health);

            final Map<CompanionFeature, Integer> features = new HashMap<>();

            final String[] featuresInitial = elements.get(5).text().split(", ");
            for(final String feature : featuresInitial) {
                features.put(ObjectUtils.getEnumForName(CompanionFeature.class, feature), 0);
            }

            final Elements featuresLate = elements.get(6).select("li");
            for(final Element feature : featuresLate) {
                final Matcher matcher = patternFeatures.matcher(feature.text());
                if(matcher.find()) {
                    features.put(
                            ObjectUtils.getEnumForName(CompanionFeature.class, matcher.group(2)),
                            Integer.parseInt(matcher.group(1)));
                } else {
                    fail(String.format("Could not parse companion id = %d features: %s", id, feature.text()));
                }
            }

            assertEquals(String.format("CompanionSpecies %s incorrect features", name),
                    features, companionSpecies.features);

            assertEquals(String.format("CompanionSpecies %s incorrect description", name),
                    document.select("blockquote").first().html(), companionSpecies.description);
        }
    }

    public void testEquipmentType() {
        final List<List<String>> table = getApiTableData(6);
        checkSize(EquipmentType.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final EquipmentType equipmentType = ObjectUtils.getEnumForCode(EquipmentType.class, code);
            assertNotNull(
                    String.format("EquipmentType not found: code = %d", code),
                    equipmentType);

            final String name = item.get(1);
            assertEquals(
                    String.format("EquipmentType incorrect name: %s", name),
                    name, equipmentType.name);
        }
    }

    public void testGameState() {
        final List<List<String>> table = getApiTableData(11);
        checkSize(GameState.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final GameState gameState = ObjectUtils.getEnumForCode(GameState.class, code);
            assertNotNull(
                    String.format("GameState not found: code = %d", code),
                    gameState);

            final String name = item.get(1);
            assertEquals(
                    String.format("GameState incorrect name: %s", name),
                    name, gameState.name);
        }
    }

    public void testGender() {
        final List<List<String>> table = getApiTableData(1);
        checkSize(Gender.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final Gender gender = ObjectUtils.getEnumForCode(Gender.class, code);
            assertNotNull(
                    String.format("Gender not found: code = %d", code),
                    gender);

            final String name = item.get(1);
            assertEquals(
                    String.format("Gender incorrect name: %s", name),
                    name, gender.name);
        }
    }

    public void testHabit() {
        final List<List<String>> table = getApiTableData(14);
        checkSize(Habit.class, table.size());

        for(final List<String> item : table) {
            final String code = item.get(0);
            final Habit habit = ObjectUtils.getEnumForCode(Habit.class, code);
            assertNotNull(
                    String.format("Habit not found: code = %s", code),
                    habit);

            final String name = item.get(1);
            assertEquals(
                    String.format("Habit incorrect name: %s", name),
                    name, habit.name);
        }
    }

    public void testHeroAction() {
        final List<List<String>> table = getApiTableData(10);
        checkSize(HeroAction.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final HeroAction heroAction = ObjectUtils.getEnumForCode(HeroAction.class, code);
            assertNotNull(
                    String.format("HeroAction not found: code = %d", code),
                    heroAction);

            final String name = item.get(1);
            assertEquals(
                    String.format("HeroAction incorrect name: %s", name),
                    name, heroAction.name);
        }
    }

    public void testProfession() {
        final List<List<String>> table = getApiTableData(8);
        checkSize(Profession.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final Profession profession = ObjectUtils.getEnumForCode(Profession.class, code);
            assertNotNull(
                    String.format("Profession not found: code = %d", code),
                    profession);

            final String name = item.get(1);
            assertEquals(
                    String.format("Profession incorrect name: %s", name),
                    name, profession.name);
        }
    }

    public void testQuestActorType() {
        final List<List<String>> table = getApiTableData(12);
        checkSize(QuestActorType.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final QuestActorType questActorType = ObjectUtils.getEnumForCode(QuestActorType.class, code);
            assertNotNull(
                    String.format("QuestActorType not found: code = %d", code),
                    questActorType);

            final String name = item.get(1);
            assertEquals(
                    String.format("QuestActorType incorrect name: %s", name),
                    name, questActorType.name);
        }
    }

    public void testRace() {
        final List<List<String>> table = getApiTableData(2);
        checkSize(Race.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final Race race = ObjectUtils.getEnumForCode(Race.class, code);
            assertNotNull(
                    String.format("Race not found: code = %d", code),
                    race);

            final String name = item.get(1);
            assertEquals(
                    String.format("Race incorrect name: %s", name),
                    name, race.name);
        }
    }

    public void testThirdPartyAuthState() {
        final List<List<String>> table = getApiTableData(0);
        checkSize(ThirdPartyAuthState.class, table.size());

        for(final List<String> item : table) {
            final int code = Integer.parseInt(item.get(0));
            final ThirdPartyAuthState thirdPartyAuthState = ObjectUtils.getEnumForCode(ThirdPartyAuthState.class, code);
            assertNotNull(
                    String.format("ThirdPartyAuthState not found: code = %d", code),
                    thirdPartyAuthState);

            final String name = item.get(1);
            assertEquals(
                    String.format("ThirdPartyAuthState incorrect name: %s", name),
                    name, thirdPartyAuthState.name);
        }
    }

    private Document getDocument(final String url, final String description) {
        final HttpClient httpClient = HttpClients.createDefault();
        final HttpUriRequest httpUriRequest = RequestUtils.getHttpGetRequest(url);
        try {
            final HttpResponse httpResponse = httpClient.execute(httpUriRequest);
            final int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
            if(httpStatusCode == HttpStatus.SC_OK) {
                final String text = EntityUtils.toString(httpResponse.getEntity());
                if(TextUtils.isEmpty(text)) {
                    fail(String.format("%s: response empty", description));
                } else {
                    final Document document = Jsoup.parse(text);
                    assertNotNull(String.format("%s: parse failed, content = %s", description, text));
                    return document;
                }
            } else {
                fail(String.format("%s: HTTP code %d", description, httpStatusCode));
            }
        } catch (IOException e) {
            fail(String.format("%s: %s", description, e.getMessage()));
        }
        return null;
    }

    private List<List<String>> getApiTableData(final int index) {
        return getTableData(apiGuide.select("table").get(index), true, 0, 1);
    }

    private List<List<String>> getTableData(final Element table, final boolean excludeHeader, final int... indices) {
        final Elements elements = table.select("tr");

        final int size = elements.size();
        final List<List<String>> result = new ArrayList<>();
        for(int i = (excludeHeader ? 1 : 0); i < size; i++) {
            final Element element = elements.get(i);
            final List<String> item = new ArrayList<>();
            for(final int index : indices) {
                item.add(element.child(index).text());
            }
            result.add(item);
        }

        return result;
    }

    private <E extends Enum<E>> void checkSize(final Class<E> clazz, final int expected) {
        final int actual = clazz.getEnumConstants().length;
        assertEquals(
                String.format("%s: incorrect values count; expected %d, actual %d",
                        clazz.getSimpleName(), expected, actual),
                expected, actual);
    }

    private <E extends Enum<E>> void checkSize(final Class<E> clazz, final int expected, final String comment) {
        final int actual = clazz.getEnumConstants().length;
        assertEquals(
                String.format("%s: incorrect values count; expected %d, actual %d - %s",
                        clazz.getSimpleName(), expected, actual, comment),
                expected, actual);
    }

}
