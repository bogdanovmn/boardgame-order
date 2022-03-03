package com.github.bogdanovmn.boardgameorder.web.etl;

import com.github.bogdanovmn.boardgameorder.core.ExcelPriceItem;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Item;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

public class ItemsMapTest {

    @Test
    public void get() {
        ItemsMap map = new ItemsMap(
            new ArrayList<Item>() {{
                add(item("Лоскутная империя (Queendomino)"));
                add(item("\"Пандемия\""));
                add(item("Стиль Жизни.Наст.игра \"Нью-Йорк 1901\""));
                add(item("\"Спящие королевы.Делюкс\" (жестян. коробка)"));
                add(item("Наст. игра \"Торт в лицо\" в коробке"));
                add(item("Наст. игра \"Оранж Квест: в погоне за Конфетным Джо\""));
                add(item("Наст. игра \"Брасс.Бирмингем\" (Crowd games)"));
                add(item("\"Номер 8\""));
                add(item("\"Номер 9\""));
                add(item("настольная игра Лови усы 8122"));
                add(item("Зв.8972 Наст.игра \"Exit.Полярная станция\""));
                add(item("Стиль Жизни.Наст.игра \"Маленький принц\""));
            }}
        );

        assertNotNull(map.get(excelItem("Лоскутная империя (Queendomino) АРТ.432017")));
        assertNotNull(map.get(excelItem("\"Пандемия\" 71100")));
        assertNotNull(map.get(excelItem("Стиль Жизни.Наст.игра \"Нью-Йорк 1901\" 02201")));
        assertNotNull(map.get(excelItem("\"Спящие королевы.Делюкс\" (жестян. коробка) 003 /12")));
        assertNotNull(map.get(excelItem("Наст. игра \"Торт в лицо\" в коробке арт.5585/6188-1/2/8121")));
        assertNotNull(map.get(excelItem("Наст. игра \"Оранж Квест: в погоне за Конфетным Джо\" РРЦ -890 руб")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) РРЦ 4500")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) РРЦ 4500р")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) РРЦ 4500руб")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) РРЦ 4500 руб")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) РРЦ 4500 р.")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) МРРЦ - 2400 руб")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) МРРЦ2400р.")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) МРРЦ2400 р.")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) (промо цена)")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) (Промо-цена)")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) (Фиксированная цена)")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) (Фикс цена)")));
        assertNotNull(map.get(excelItem("Наст. игра \"Брасс.Бирмингем\" (Crowd games) (Фикс. цена)")));
        assertNotNull(map.get(excelItem("\"Номер 8\" №SL-00429")));
        assertNotNull(map.get(excelItem("\"Номер 9\" LS11")));
        assertNotNull(map.get(excelItem("Новинка \"Номер 9\" LS11")));
        assertNotNull(map.get(excelItem("Новинка. \"Номер 9\" LS11 (Crowd games)")));
        assertNotNull(map.get(excelItem("Наст. игра \"Лови усы\" арт.8122")));
        assertNotNull(map.get(excelItem("Зв.8972 Exit.Полярная станция")));
        assertNotNull(map.get(excelItem("Стиль Жизни.Наст.игра \"Маленький принц\" LPP17RU1")));
    }

    private ExcelPriceItem excelItem(String title) {
        return new ExcelPriceItem("test group", title, null, null, "test barcode", null);
    }

    private Item item(String title) {
        return new Item().setTitle(title);
    }
}