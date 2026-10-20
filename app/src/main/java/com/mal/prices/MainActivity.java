
package com.mal.prices;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.res.ColorStateList;
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
        5000,3000,2500,1500,4000,2000,3500,500,1000,1800,
        2200,2000,1200,2500,700,3000,300,200,1000,500,
        1500,500,5000,2000,800,1200,1500,2000,700,2500,
        1000,500,800,3000,300,1500,2000,3500,2500,50000,
        500,700,200,1000,5000,4000,1500,2500,5000,200,
        800,3000,300,100,5000,7000,10000,1000,4000,12000
    };

    EditText input;
    TextView result;

    int green = Color.rgb(12, 75, 48);
    int gold = Color.rgb(232, 190, 85);
    int dark = Color.rgb(25, 49, 38);

    GradientDrawable rounded(int color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(radius);
        return shape;
    }

    TextView makeText(String text, int size, int color) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(8, 53, 36));
        getWindow().setNavigationBarColor(Color.rgb(8, 53, 36));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 30, 24, 30);
        layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        GradientDrawable background = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            new int[] {
                Color.rgb(7, 54, 38),
                Color.rgb(15, 105, 68),
                Color.rgb(3, 65, 48)
            }
        );
        layout.setBackground(background);

        TextView icon = makeText("🛒", 48, Color.WHITE);
        icon.setGravity(Gravity.CENTER);
        layout.addView(icon);

        TextView title = makeText("أسعار البضائع", 30, gold);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 8, 0, 4);
        layout.addView(title);

        TextView subtitle = makeText(
            "احسب قيمة مشترياتك بسهولة",
            16,
            Color.WHITE
        );
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 0, 0, 28);
        layout.addView(subtitle);

        TextView label = makeText("أدخل المنتجات", 18, gold);
        label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        label.setPadding(0, 0, 0, 10);
        layout.addView(label);

        input = new EditText(this);
        input.setSingleLine(false);
        input.setMinLines(2);
        input.setTextSize(18);
        input.setTextColor(dark);
        input.setHintTextColor(Color.GRAY);
        input.setHint("مثال: زيت سكر رز");
        input.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        input.setPadding(18, 16, 18, 16);
        input.setBackground(rounded(Color.WHITE, 22));
        layout.addView(input);

        TextView tip = makeText(
            "اكتب أسماء المنتجات وافصل بينها بمسافة",
            13,
            Color.rgb(225, 241, 231)
        );
        tip.setPadding(0, 10, 0, 20);
        layout.addView(tip);

        Button search = new Button(this);
        search.setText("احسب المجموع");
        search.setTextSize(18);
        search.setTextColor(green);
        search.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        search.setAllCaps(false);
        search.setBackground(rounded(gold, 22));
        search.setBackgroundTintList(null);
        search.setPadding(10, 12, 10, 12);
        layout.addView(search);

        TextView resultTitle = makeText("تفاصيل الحساب", 20, gold);
        resultTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        resultTitle.setPadding(0, 28, 0, 12);
        layout.addView(resultTitle);

        result = makeText(
            "ستظهر هنا أسعار المنتجات والمجموع النهائي.",
            17,
            dark
        );
        result.setGravity(Gravity.RIGHT);
        result.setPadding(18, 20, 18, 20);
        result.setMinHeight(100);
        result.setBackground(rounded(Color.WHITE, 20));
        layout.addView(result);

        TextView footer = makeText(
            "أسعار البضائع • تسوّق بذكاء",
            13,
            Color.rgb(230, 222, 183)
        );
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, 28, 0, 8);
        layout.addView(footer);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(green);
        scroll.addView(layout);
        setContentView(scroll);

        search.setOnClickListener(v -> calculate());
    }

    void calculate() {
        String text = input.getText().toString().trim();

        if (text.isEmpty()) {
            result.setText("اكتب اسم منتج أولاً.");
            return;
        }

        String[] items = text.split("\\s+");
        int total = 0;
        int count = 0;
        StringBuilder output = new StringBuilder();

        for (String item : items) {
            if (item.equals("و")) continue;

            if (item.startsWith("و") && item.length() > 1) {
                item = item.substring(1);
            }

            boolean found = false;

            for (int i = 0; i < names.length; i++) {
                if (item.equals(names[i])) {
                    output.append(names[i])
                          .append(": ")
                          .append(prices[i])
                          .append(" ريال\n");

                    total += prices[i];
                    count++;
                    found = true;
                    break;
                }
            }

            if (!found) {
                output.append("غير موجود: ")
                      .append(item)
                      .append("\n");
            }
        }

        if (count > 0) {
            output.append("\nعدد المنتجات: ")
                  .append(count)
                  .append("\nالمجموع: ")
                  .append(total)
                  .append(" ريال");
        }

        result.setText(output.toString());
    }
}
