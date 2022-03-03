package com.github.bogdanovmn.boardgameorder.web.orm;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.Item;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemTest {

    @Test
    public void getHtmlTitle() {
        assertEquals(
            "blabla \"<b>Game Name</b>\" blabla",
            new Item().setTitle("blabla \"Game Name\" blabla").getHtmlTitle()
        );

        assertEquals(
            "Наст.игра МХ \"<b>Pathfinder \"Возвращение Рунных Властителей.</b>\" арт.1424",
            new Item().setTitle("Наст.игра МХ \"Pathfinder \"Возвращение Рунных Властителей.\" арт.1424").getHtmlTitle()
        );
    }

    @Test
    public void getEffectiveTitle() {
        assertEquals(
            "Game Name",
            new Item().setTitle("blabla \"Game Name\" blabla").getEffectiveTitle()
        );

        assertEquals(
            "Pathfinder Возвращение Рунных Властителей.",
            new Item().setTitle("Наст.игра МХ \"Pathfinder \"Возвращение Рунных Властителей.\" арт.1424").getEffectiveTitle()
        );
    }

    @Test
    public void shouldDetectFixPrice() {
        assertTrue(
            new Item().setTitle("Hasbro Наст. игра \"Монополия\" классическая арт.С1009 (Фикс.цена)").isLikeFixPriceTitle()
        );

        assertTrue(
            new Item().setTitle("\"Профессор Зло.Цитадель времени\"(фикс цена) РРЦ 2700 руб").isLikeFixPriceTitle()
        );

        assertTrue(
            new Item().setTitle("Промо. Hasbro Наст. игра \"Монополия\" дорожная арт.B1002 (Фикс. цена) Упаковка").isLikeFixPriceTitle()
        );

        assertTrue(
            new Item().setTitle("Наст. игра \"Путешествия Марко Поло\" (Crowd games) Фиксированная цена").isLikeFixPriceTitle()
        );
    }

    @Test
    public void shouldNotDetectFixPrice() {
        assertFalse(
            new Item().setTitle("Фиксики арт.С1009 цена 100500р").isLikeFixPriceTitle()
        );

        assertFalse(
            new Item().setTitle("Наст. игра \"Сезоны\" (Crowd games) РРЦ 3690").isLikeFixPriceTitle()
        );
    }
}