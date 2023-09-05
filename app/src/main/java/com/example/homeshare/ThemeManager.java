package com.example.homeshare;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.ImageViewCompat;

import com.google.android.material.chip.Chip;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ThemeManager {
    public static Map<Integer, Integer> light_to_dark;
    public static Map<Integer, Integer> dark_to_light;
    public static Theme activeTheme = Theme.DARK;

    enum Theme {
        LIGHT,
        DARK
    }

    static {
        light_to_dark = new HashMap<Integer, Integer>();
        dark_to_light = new HashMap<Integer, Integer>();

        Resources res = MainActivity.instance.getResources();

        dark_to_light.put(res.getColor(R.color.background),
                res.getColor(R.color.lt_background));
        dark_to_light.put(res.getColor(R.color.background_light),
                res.getColor(R.color.lt_background_light));
        dark_to_light.put(res.getColor(R.color.text_primary),
                res.getColor(R.color.lt_text_primary));
        dark_to_light.put(res.getColor(R.color.text_primary_light),
                res.getColor(R.color.lt_text_primary_light));
        dark_to_light.put(res.getColor(R.color.text_secondary),
                res.getColor(R.color.lt_text_secondary));
        dark_to_light.put(res.getColor(R.color.bg_chip_checked),
                res.getColor(R.color.lt_bg_chip_checked));
        dark_to_light.put(res.getColor(R.color.bg_chip),
                res.getColor(R.color.lt_bg_chip));
        dark_to_light.put(res.getColor(R.color.accent),
                res.getColor(R.color.lt_accent));
        dark_to_light.put(res.getColor(R.color.accent_secondary),
                res.getColor(R.color.lt_accent_secondary));
        dark_to_light.put(res.getColor(R.color.accent_tertiary),
                res.getColor(R.color.lt_accent_tertiary));

        for(Map.Entry<Integer, Integer> entry : dark_to_light.entrySet()){
            light_to_dark.put(entry.getValue(), entry.getKey());
        }
    }

    private static int colorInverse(Theme theme, int color) {
        if (theme == Theme.LIGHT) {
            return dark_to_light.getOrDefault(color, color);
        } else {
            return light_to_dark.getOrDefault(color, color);
        }
    }

    private static void chipColorListInverse(Theme theme, Chip chip) {
        if (!chip.getChipBackgroundColor().isStateful()) {
            int color = colorInverse(theme, chip.getChipBackgroundColor().getDefaultColor());
            chip.setChipBackgroundColor(ColorStateList.valueOf(color));
            return;
        }

        if (theme == Theme.LIGHT) {
            chip.setChipBackgroundColorResource(R.color.lt_chip_color_list);
        } else {
            chip.setChipBackgroundColorResource(R.color.chip_color_list);
        }
    }

    private static void setColorTints() {
        MainActivity.instance.tabButtons.get(MainActivity.Tab.DISCOVER).setColorFilter(ThemeManager.getThemeColor(R.color.text_secondary));
        MainActivity.instance.tabButtons.get(MainActivity.Tab.INBOX).setColorFilter(ThemeManager.getThemeColor(R.color.text_secondary));
        MainActivity.instance.tabButtons.get(MainActivity.Tab.PROFILE).setColorFilter(ThemeManager.getThemeColor(R.color.text_secondary));

        MainActivity.instance.tabButtons.get(MainActivity.instance.currentTab).setColorFilter(ThemeManager.getThemeColor(R.color.accent_tertiary));
    }

    private static void changeColor(Theme theme, View view) {
        // Background
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            int bgColor = colorInverse(theme, ((ColorDrawable) background).getColor());
            view.setBackgroundColor(bgColor);
        } else if (background instanceof ShapeDrawable) {
            Paint p = ((ShapeDrawable) background).getPaint();
            int bgColor = colorInverse(theme, p.getColor());
            p.setColor(bgColor);
        } else if (background instanceof GradientDrawable) {
            GradientDrawable bgGradient = (GradientDrawable) background;
            int bgColor = colorInverse(theme, bgGradient.getColor().getDefaultColor());
            bgGradient.setColor(bgColor);
        }

        if (view instanceof Chip) {
            Chip chip = (Chip) view;
            chipColorListInverse(theme, chip);

            int textColor = colorInverse(theme, chip.getCurrentTextColor());
            chip.setTextColor(textColor);

            if (chip.getChipIconTint() != null) {
                int iconTint = colorInverse(theme, chip.getChipIconTint().getDefaultColor());
                chip.setChipIconTint(ColorStateList.valueOf(iconTint));
            }
        }

        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            int textColor = colorInverse(theme, tv.getCurrentTextColor());
            tv.setTextColor(textColor);
        }

        if (view.getTag() != null && view.getTag().equals("triangle")) {
            if (theme == Theme.LIGHT) {
                ((ImageView) view).setColorFilter(MainActivity.instance.getResources().getColor(R.color.lt_background_light));
            } else {
                ((ImageView) view).setColorFilter(MainActivity.instance.getResources().getColor(R.color.background_light));
            }
        }
    }

    private static void setTheme(Theme theme, ViewGroup parent) {
        changeColor(theme, parent);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);

            if (v instanceof ViewGroup) {
                setTheme(theme, (ViewGroup) v);
            } else {
                changeColor(theme, v);
            }
        }
    }

    public static void setThemeParent(Theme theme, ViewGroup parent) {
        if (theme == activeTheme)
            return;

        setTheme(theme, parent);
        activeTheme = theme;
        setColorTints();
    }

    public static void loadTheme(ViewGroup parent) {
        setTheme(activeTheme, parent);
    }

    public static int getThemeColor(int colorResource) {
        int color = MainActivity.instance.getResources().getColor(colorResource);
        if (activeTheme == Theme.LIGHT) {
            return colorInverse(activeTheme, color);
        }

        return color;
    }
}
