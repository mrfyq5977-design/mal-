
package com.mal.prices;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity {

    String[] names = {
        "رز", "سكر", "زيت", "حليب", "دقيق",
        "شاي", "قهوه", "ملح", "مكرونه", "عدس",
        "فاصوليا", "حمص", "فول", "جبن", "زبادي",
        "بيض", "خبز", "ماء", "عصير", "بسكويت",
        "شوكولاته", "حلوى", "عسل", "مربى", "خل",
        "كاتشب", "مايونيز", "منظف", "صابون", "شامبو",
        "معجون", "فرشاة", "مناديل", "مسحوق", "اسفنج",
        "بطاريات", "مصباح", "شاحن", "سماعه", "هاتف",
        "كوب", "صحن", "ملعقه", "سكين", "قدر",
        "مقلاه", "دلو", "مكنسه", "حقيبه", "قلم",
        "دفتر", "كتاب", "مسطره", "ممحاه", "قميص",
        "بنطال", "حذاء", "جورب", "غطاء", "بطانيه"
    };

    int[] prices = {
        5000,3000,2500,1500,4000,
        2000,3500,500,1000,1800,
        2200,2000,1200,2500,700,
        3000,300,200,1000,500,
        1500,500,5000,2000,800,
        1200,1500,2000,700,2500,
        1000,500,800,3000,300,
        1500,2000,3500,2500,50000,
        500,700,200,1000,5000,
        4000,1500,2500,5000,200,
        800,3000,300,100,5000,
        7000,10000,1000,4000,12000
    };

    int[] quantities = new int[60];

    int green = Color.rgb(12, 75, 48);
    int gold = Color.rgb(232, 190, 85);
    int dark = Color.rgb(25, 49, 38);

    EditText searchBox;
    TextView totalText;
    TextView cartText;
    GridLayout productsGrid;

    GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(7, 54, 38));
        getWindow().setNavigationBarColor(Color.rgb(7, 54, 38));

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(16, 20, 16, 20);
        page.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            new int[] {
                Color.rgb(7, 54, 38),
                Color.rgb(18, 105, 68),
                Color.rgb(5, 60, 43)
            }
        ));

        TextView title = text("🛒 أسعار البضائع", 28, gold);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        page.addView(title);

        TextView subtitle = text(
            "جميع المنتجات وأسعارها",
            16, Color.WHITE
        );
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 8, 0, 18);
        page.addView(subtitle);

        searchBox = new EditText(this);
        searchBox.setSingleLine(true);
        searchBox.setTextSize(17);
        searchBox.setTextColor(dark);
        searchBox.setHintTextColor(Color.GRAY);
        searchBox.setHint("🔍 ابحث عن منتج...");
        searchBox.setPadding(16, 10, 16, 10);
        searchBox.setBackground(shape(Color.WHITE, 18));
        page.addView(searchBox);

        TextView instruction = text(
            "اضغط على المنتج لإضافته إلى الحساب",
            14, Color.WHITE
        );
        instruction.setPadding(0, 12, 0, 12);
        page.addView(instruction);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);

        productsGrid = new GridLayout(this);
        productsGrid.setColumnCount(2);
        productsGrid.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        scroll.addView(productsGrid);

        page.addView(scroll, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1
        ));

        LinearLayout summary = new LinearLayout(this);
        summary.setOrientation(LinearLayout.VERTICAL);
        summary.setPadding(14, 12, 14, 12);
        summary.setBackground(shape(Color.WHITE, 18));

        totalText = text("المجموع: 0 ريال", 20, green);
        totalText.setTypeface(null, Typeface.BOLD);
        summary.addView(totalText);

        cartText = text("لم تختر أي منتج بعد", 14, dark);
        cartText.setPadding(0, 8, 0, 4);
        summary.addView(cartText);

        Button clear = new Button(this);
        clear.setText("مسح الحساب");
        clear.setTextColor(green);
        clear.setAllCaps(false);
        clear.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(gold)
        );
        summary.addView(clear);

        page.addView(summary);

        setContentView(page);

        showProducts("");

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                CharSequence s, int start, int count, int after
            ) {}

            @Override
            public void onTextChanged(
                CharSequence s, int start, int before, int count
            ) {
                showProducts(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        clear.setOnClickListener(v -> {
            for (int i = 0; i < quantities.length; i++) {
                quantities[i] = 0;
            }
            updateSummary();
        });
    }

    void showProducts(String query) {
        productsGrid.removeAllViews();

        int shown = 0;

        for (int i = 0; i < names.length; i++) {
            if (!names[i].contains(query)) continue;

            final int index = i;
            shown++;

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setPadding(6, 12, 6, 12);
            card.setBackground(shape(Color.WHITE, 16));
            card.setClickable(true);
            card.setFocusable(true);

            TextView name = text(names[i], 17, green);
            name.setTypeface(null, Typeface.BOLD);
            name.setGravity(Gravity.CENTER);
            card.addView(name);

            TextView price = text(
                String.format(Locale.getDefault(), "%,d ريال", prices[i]),
                14, Color.rgb(155, 112, 20)
            );
            price.setGravity(Gravity.CENTER);
            price.setPadding(0, 6, 0, 0);
            card.addView(price);

            TextView quantity = text(
                "الكمية: " + quantities[i],
                13, dark
            );
            quantity.setGravity(Gravity.CENTER);
            quantity.setPadding(0, 5, 0, 0);
            card.addView(quantity);

            GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(
                GridLayout.UNDEFINED, 1f
            );
            params.setMargins(5, 5, 5, 5);

            productsGrid.addView(card, params);

            card.setOnClickListener(v -> {
                quantities[index]++;
                quantity.setText("الكمية: " + quantities[index]);
                updateSummary();
            });
        }

        if (shown == 0) {
            TextView empty = text(
                "لم يتم العثور على المنتج",
                17, Color.WHITE
            );
            empty.setPadding(12, 20, 12, 20);
            productsGrid.addView(empty);
        }
    }

    void updateSummary() {
        long total = 0;
        StringBuilder selected = new StringBuilder();

        for (int i = 0; i < names.length; i++) {
            if (quantities[i] > 0) {
                total += (long) prices[i] * quantities[i];

                selected.append(names[i])
                    .append(" × ")
                    .append(quantities[i])
                    .append("\n");
            }
        }

        totalText.setText(String.format(
            Locale.getDefault(),
            "المجموع: %,d ريال", total
        ));

        if (selected.length() == 0) {
            cartText.setText("لم تختر أي منتج بعد");
        } else {
            cartText.setText(selected.toString());
        }
    }
}
