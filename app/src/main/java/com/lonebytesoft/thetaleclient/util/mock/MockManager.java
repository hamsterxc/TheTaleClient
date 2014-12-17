package com.lonebytesoft.thetaleclient.util.mock;

import com.lonebytesoft.thetaleclient.api.dictionary.QuestType;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Game info response mocking manager
 * Set mockers to alter response on consecutive calls to {@link #mock}
 * {@code start} is step number for a mocker to start its altering,
 * {@code end} is step number for it to end its work
 * Use {@code -1} for {@code end} for infinite range
 * 
 * @author Hamster
 * @since 17.12.2014
 *
 * Usage example:
 *     mockManager = new MockManager();
 *
 *     // consecutive events
 *     mockManager.setAlive(true, 0, 1);
 *     mockManager.setIdle(false, 0, 4);
 *     mockManager.setEnergy(0, 0, 7);
 *     mockManager.setHealth(999, 0, 10);
 *     mockManager.setNewMessages(0, 0, 13);
 *     mockManager.setQuestChoice(false, 0, 16);
 *
 *     mockManager.setAlive(false, 2, 3);
 *     mockManager.setAlive(true, 4, -1);
 *     mockManager.setIdle(true, 5, 6);
 *     mockManager.setIdle(false, 7, -1);
 *     mockManager.setEnergy(20, 8, 9);
 *     mockManager.setEnergy(0, 10, -1);
 *     mockManager.setHealth(199, 11, 12);
 *     mockManager.setHealth(999, 13, -1);
 *     mockManager.setNewMessages(9, 14, 15);
 *     mockManager.setNewMessages(0, 16, -1);
 *     mockManager.setQuestChoice(true, 17, 18);
 *     mockManager.setQuestChoice(false, 19, -1);
 *     
 *     (...)
 *     
 *     response = mockManager.mock(response);
 *     
 * Here we first set "normal" parameters in the response,
 * then we mock the response to consecutively make hero dead, idle, etc.
 */
public class MockManager {

    private List<MockerItem> mockers = new ArrayList<>();
    private int step = 0;

    public void reset() {
        mockers.clear();
        step = 0;
    }

    public GameInfoResponse mock(final GameInfoResponse response) {
        GameInfoResponse gameInfoResponse = response;
        for(final MockerItem mockerItem : mockers) {
            if((step >= mockerItem.start) && ((mockerItem.end < 0) || (step <= mockerItem.end))) {
                gameInfoResponse = mockerItem.mocker.mock(gameInfoResponse);
            }
        }
        step++;
        return gameInfoResponse;
    }

    public void addMocker(final Mocker mocker, final int start) {
        mockers.add(new MockerItem(mocker, start, -1));
    }

    public void addMocker(final Mocker mocker, final int start, final int end) {
        mockers.add(new MockerItem(mocker, start, end));
    }

    public void removeMocker(final Mocker mocker, final int step) {
        final MockerItem mockerItem = findMockerItem(mocker);
        if(mockerItem != null) {
            mockerItem.end = step;
        }
    }

    private MockerItem findMockerItem(final Mocker mocker) {
        for(final MockerItem mockerItem : mockers) {
            if((mockerItem.mocker == mocker) && (mockerItem.end < 0)) {
                return mockerItem;
            }
        }
        return null;
    }

    // helpers

    public void setAlive(final boolean isAlive, final int start, final int end) {
        addMocker(new Mocker() {
            @Override
            public GameInfoResponse mock(GameInfoResponse response) {
                try {
                    final JSONObject json = new JSONObject(response.rawResponse);
                    json.getJSONObject("data").getJSONObject("account").getJSONObject("hero").getJSONObject("base").put("alive", isAlive);
                    return new GameInfoResponse(json.toString());
                } catch(JSONException e) {
                    return response;
                }
            }
        }, start, end);
    }

    public void setIdle(final boolean isIdle, final int start, final int end) {
        addMocker(new Mocker() {
            @Override
            public GameInfoResponse mock(GameInfoResponse response) {
                try {
                    final JSONObject json = new JSONObject(response.rawResponse);
                    final JSONArray quests = json.getJSONObject("data").getJSONObject("account").getJSONObject("hero").getJSONObject("quests").getJSONArray("quests");
                    quests.getJSONObject(quests.length() - 1).getJSONArray("line").getJSONObject(0).put("type", isIdle ? QuestType.NO_QUEST.getCode() : QuestType.SPENDING.getCode());
                    return new GameInfoResponse(json.toString());
                } catch(JSONException e) {
                    return response;
                }
            }
        }, start, end);
    }

    public void setEnergy(final int energy, final int start, final int end) {
        addMocker(new Mocker() {
            @Override
            public GameInfoResponse mock(GameInfoResponse response) {
                try {
                    final JSONObject json = new JSONObject(response.rawResponse);
                    json.getJSONObject("data").getJSONObject("account").getJSONObject("hero").getJSONObject("energy").put("value", energy);
                    return new GameInfoResponse(json.toString());
                } catch(JSONException e) {
                    return response;
                }
            }
        }, start, end);
    }

    public void setHealth(final int health, final int start, final int end) {
        addMocker(new Mocker() {
            @Override
            public GameInfoResponse mock(GameInfoResponse response) {
                try {
                    final JSONObject json = new JSONObject(response.rawResponse);
                    json.getJSONObject("data").getJSONObject("account").getJSONObject("hero").getJSONObject("base").put("health", health);
                    return new GameInfoResponse(json.toString());
                } catch(JSONException e) {
                    return response;
                }
            }
        }, start, end);
    }

    public void setNewMessages(final int newMessages, final int start, final int end) {
        addMocker(new Mocker() {
            @Override
            public GameInfoResponse mock(GameInfoResponse response) {
                try {
                    final JSONObject json = new JSONObject(response.rawResponse);
                    json.getJSONObject("data").getJSONObject("account").put("new_messages", newMessages);
                    return new GameInfoResponse(json.toString());
                } catch(JSONException e) {
                    return response;
                }
            }
        }, start, end);
    }

    public void setQuestChoice(final boolean isQuestChoiceAvailable, final int start, final int end) {
        addMocker(new Mocker() {
            @Override
            public GameInfoResponse mock(GameInfoResponse response) {
                try {
                    final JSONObject json = new JSONObject(response.rawResponse);
                    final JSONArray quests = json.getJSONObject("data").getJSONObject("account").getJSONObject("hero").getJSONObject("quests").getJSONArray("quests");
                    final JSONArray lines = quests.getJSONObject(quests.length() - 1).getJSONArray("line");
                    lines.getJSONObject(lines.length() - 1).put("choice_alternatives",
                            new JSONArray(isQuestChoiceAvailable ? "[[\"11\",\"12\"],[\"21\",\"22\"]]" : "[]"));
                    return new GameInfoResponse(json.toString());
                } catch(JSONException e) {
                    return response;
                }
            }
        }, start, end);
    }

    private class MockerItem {
        public Mocker mocker;
        public int start;
        public int end;

        public MockerItem(final Mocker mocker, final int start, final int end) {
            this.mocker = mocker;
            this.start = start;
            this.end = end;
        }
    }

}
