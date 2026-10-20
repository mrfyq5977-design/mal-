
package com.mal.prices;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(28, 28, 28, 28);
        layout.setBackgroundColor(Color.rgb(245, 247, 250));

        TextView title = new TextView(this);
        title.setText("أسعار البضائع");
        title.setTextSize(28);
        title.setTextColor(Color.rgb(20, 70, 130));
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView hint = new TextView(this);
        hint.setText("اكتب أسماء المنتجات مفصولة بمسافة");
        hint.setTextSize(16);
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(0, 20, 0, 12);
        layout.addView(hint);

        input = new EditText(this);
        input.setSingleLine(false);
        input.setHint("مثال: زيت سكر رز");
        input.setTextSize(18);
        input.setGravity(Gravity.RIGHT);
        layout.addView(input);

        Button search = new Button(this);
        search.setText("اعرض السعر");
        layout.addView(search);

        result = new TextView(this);
        result.setTextSize(18);
        result.setTextColor(Color.BLACK);
        result.setPadding(0, 24, 0, 0);
        layout.addView(result);

        ScrollView scroll = new ScrollView(this);
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
                      .append(item).append("\n");
            }
        }

        if (count > 0) {
            output.append("\nعدد المنتجات: ").append(count)
                  .append("\nالمجموع: ").append(total)
                  .append(" ريال");
        }

        result.setText(output.toString());
    }
}
