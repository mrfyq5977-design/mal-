
package com.mal.prices;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    int green = Color.rgb(12, 75, 48);
    int gold = Color.rgb(232, 190, 85);
    int dark = Color.rgb(25, 49, 38);

    LinearLayout root, content, tabs;
    SharedPreferences prefs;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<Integer> prices = new ArrayList<>();
    ArrayList<Integer> stock = new ArrayList<>();
    ArrayList<Integer> quantities = new ArrayList<>();

    long totalSales = 0;
    int invoiceCount = 0;
    int currentTab = 0;

    EditText searchBox;
    TextView totalText, cartText;

    String[] initialNames = {
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

    int[] initialPrices = {
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

    LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(green);
        b.setAllCaps(false);
        b.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(gold)
        );
        return b;
    }

    void heading(LinearLayout parent, String label) {
        TextView t = text(label, 23, gold);
        t.setTypeface(null, Typeface.BOLD);
        t.setGravity(Gravity.CENTER);
        t.setPadding(0, 12, 0, 18);
        parent.addView(t);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(7, 54, 38));
        getWindow().setNavigationBarColor(Color.rgb(7, 54, 38));

        prefs = getSharedPreferences("prices_data", MODE_PRIVATE);
        loadData();

        root = column();
        root.setPadding(12, 12, 12, 8);
        root.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{
                Color.rgb(7, 54, 38),
                Color.rgb(18, 105, 68),
                Color.rgb(5, 60, 43)
            }
        ));

        TextView title = text("🛒 أسعار البضائع", 27, gold);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 6, 0, 12);
        root.addView(title);

        tabs = new LinearLayout(this);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setBackground(shape(Color.WHITE, 14));
        root.addView(tabs);

        content = column();
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(
            -1, 0, 1
        ));

        setContentView(root);
        showTab(0);
    }

    void addTabButton(String label, int tab) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(12);
        b.setAllCaps(false);
        b.setPadding(2, 8, 2, 8);
        b.setTextColor(currentTab == tab ? gold : green);
        b.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(
                currentTab == tab ? green : Color.WHITE
            )
        );

        tabs.addView(b, new LinearLayout.LayoutParams(
            0, -2, 1
        ));

        b.setOnClickListener(v -> showTab(tab));
    }

    void showTab(int tab) {
        currentTab = tab;
        tabs.removeAllViews();
        content.removeAllViews();

        addTabButton("🧾 فاتورة", 0);
        addTabButton("📊 الإحصاء", 1);
        addTabButton("📦 الأصناف", 2);

        if (tab == 0) showInvoice();
        else if (tab == 1) showStatistics();
        else showAddProduct();
    }

    void showInvoice() {
        heading(content, "إنشاء فاتورة");

        searchBox = new EditText(this);
        searchBox.setSingleLine(true);
        searchBox.setTextSize(16);
        searchBox.setTextColor(dark);
        searchBox.setHint("ابحث عن صنف...");
        searchBox.setBackground(shape(Color.WHITE, 14));
        searchBox.setPadding(12, 8, 12, 8);
        content.addView(searchBox);

        TextView help = text(
            "اضغط على الصنف لإضافته إلى الفاتورة",
            14, Color.WHITE
        );
        help.setPadding(0, 10, 0, 10);
        content.addView(help);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(2);
        grid.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ScrollView listScroll = new ScrollView(this);
        listScroll.setFillViewport(false);
        listScroll.addView(grid);
        content.addView(listScroll, new LinearLayout.LayoutParams(
            -1, 0, 1
        ));

        totalText = text("الإجمالي: 0 ريال", 21, green);
        totalText.setTypeface(null, Typeface.BOLD);

        cartText = text("لا توجد أصناف في الفاتورة", 15, dark);

        LinearLayout summary = column();
        summary.setPadding(12, 12, 12, 12);
        summary.setBackground(shape(Color.WHITE, 16));
        summary.addView(totalText);
        summary.addView(cartText);

        Button save = button("حفظ الفاتورة");
        Button clear = button("مسح الفاتورة");

        summary.addView(save);
        summary.addView(clear);
        content.addView(summary);

        Runnable refresh = () -> {
            grid.removeAllViews();
            String query = searchBox.getText().toString().trim();

            for (int i = 0; i < names.size(); i++) {
                if (!names.get(i).contains(query)) continue;

                final int index = i;

                LinearLayout card = column();
                card.setGravity(Gravity.CENTER);
                card.setPadding(5, 12, 5, 12);
                card.setBackground(shape(Color.WHITE, 14));

                TextView n = text(names.get(i), 16, green);
                n.setTypeface(null, Typeface.BOLD);
                n.setGravity(Gravity.CENTER);
                card.addView(n);

                TextView p = text(
                    String.format(
                        Locale.getDefault(), "%,d ريال",
                        prices.get(i)
                    ), 14, dark
                );
                p.setGravity(Gravity.CENTER);
                card.addView(p);

                TextView q = text(
                    "الكمية: " + quantities.get(i),
                    13, dark
                );
                q.setGravity(Gravity.CENTER);
                card.addView(q);

                GridLayout.LayoutParams gp =
                    new GridLayout.LayoutParams();
                gp.width = 0;
                gp.height = -2;
                gp.columnSpec = GridLayout.spec(
                    GridLayout.UNDEFINED, 1f
                );
                gp.setMargins(4, 4, 4, 4);
                grid.addView(card, gp);

                card.setOnClickListener(v -> {
                    quantities.set(index, quantities.get(index) + 1);
                    q.setText("الكمية: " + quantities.get(index));
                    updateInvoice();
                });

                card.setOnLongClickListener(v -> {
                    if (quantities.get(index) > 0) {
                        quantities.set(index, quantities.get(index) - 1);
                        q.setText("الكمية: " + quantities.get(index));
                        updateInvoice();
                    }
                    return true;
                });
            }
        };

        searchBox.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(
                CharSequence s, int start, int count, int after
            ) {}
            public void onTextChanged(
                CharSequence s, int start, int before, int count
            ) { refresh.run(); }
            public void afterTextChanged(android.text.Editable e) {}
        });

        save.setOnClickListener(v -> {
            long total = calculateTotal();

            if (total == 0) {
                Toast.makeText(this, "أضف صنفًا أولًا",
                    Toast.LENGTH_SHORT).show();
                return;
            }

            totalSales += total;
            invoiceCount++;

            for (int i = 0; i < names.size(); i++) {
                if (quantities.get(i) > 0) {
                    stock.set(i, Math.max(
                        0, stock.get(i) - quantities.get(i)
                    ));
                }
                quantities.set(i, 0);
            }

            saveData();
            showTab(1);

            Toast.makeText(this, "تم حفظ الفاتورة",
                Toast.LENGTH_SHORT).show();
        });

        clear.setOnClickListener(v -> {
            for (int i = 0; i < quantities.size(); i++) {
                quantities.set(i, 0);
            }
            updateInvoice();
            refresh.run();
        });

        refresh.run();
        updateInvoice();
    }

    long calculateTotal() {
        long total = 0;
        for (int i = 0; i < names.size(); i++) {
            total += (long) prices.get(i) * quantities.get(i);
        }
        return total;
    }

    void updateInvoice() {
        if (totalText == null || cartText == null) return;

        totalText.setText(String.format(
            Locale.getDefault(),
            "الإجمالي: %,d ريال", calculateTotal()
        ));

        StringBuilder selected = new StringBuilder();

        for (int i = 0; i < names.size(); i++) {
            if (quantities.get(i) > 0) {
                selected.append(names.get(i))
                    .append(" × ")
                    .append(quantities.get(i))
                    .append(" = ")
                    .append(String.format(
                        Locale.getDefault(), "%,d",
                        (long) prices.get(i) * quantities.get(i)
                    ))
                    .append(" ريال\n");
            }
        }

        cartText.setText(selected.length() == 0
            ? "لا توجد أصناف في الفاتورة"
            : selected.toString());
    }

    void showStatistics() {
        heading(content, "الإحصاء والتقارير");

        addStatCard("عدد الفواتير", String.valueOf(invoiceCount));
        addStatCard("إجمالي المبيعات",
            String.format(Locale.getDefault(), "%,d ريال", totalSales));

        int totalItems = 0;
        for (int s : stock) totalItems += s;
        addStatCard("إجمالي الكمية المتبقية",
            String.valueOf(totalItems));

        TextView note = text(
            "الإحصاءات تبدأ من وقت استخدام هذه النسخة، "
            + "ولا تشمل فواتير سابقة لم تُسجل داخل التطبيق.",
            14, Color.WHITE
        );
        note.setPadding(4, 14, 4, 14);
        content.addView(note);

        Button back = button("إنشاء فاتورة جديدة");
        content.addView(back);
        back.setOnClickListener(v -> showTab(0));
    }

    void addStatCard(String label, String value) {
        LinearLayout card = column();
        card.setPadding(16, 18, 16, 18);
        card.setBackground(shape(Color.WHITE, 16));

        TextView t = text(label, 17, green);
        t.setTypeface(null, Typeface.BOLD);
        card.addView(t);

        TextView val = text(value, 23, dark);
        val.setPadding(0, 8, 0, 0);
        card.addView(val);

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 6, 0, 10);
        content.addView(card, lp);
    }

    void showAddProduct() {
        heading(content, "إدخال صنف جديد");

        EditText name = field("اسم الصنف");
        EditText price = field("السعر بالريال");
        EditText amount = field("الكمية المتوفرة");

        price.setInputType(2);
        amount.setInputType(2);

        content.addView(name);
        content.addView(price);
        content.addView(amount);

        Button add = button("إضافة الصنف");
        content.addView(add);

        add.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String p = price.getText().toString().trim();
            String a = amount.getText().toString().trim();

            if (n.isEmpty() || p.isEmpty() || a.isEmpty()) {
                Toast.makeText(this, "أكمل جميع الحقول",
                    Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int priceValue = Integer.parseInt(p);
                int stockValue = Integer.parseInt(a);

                if (priceValue < 0 || stockValue < 0) {
                    Toast.makeText(this, "أدخل أرقامًا غير سالبة",
                        Toast.LENGTH_SHORT).show();
                    return;
                }

                names.add(n);
                prices.add(priceValue);
                stock.add(stockValue);
                quantities.add(0);

                saveData();
                Toast.makeText(this, "تمت إضافة الصنف",
                    Toast.LENGTH_SHORT).show();

                showAddProduct();
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "السعر والكمية يجب أن يكونا أرقامًا",
                    Toast.LENGTH_SHORT).show();
            }
        });

        TextView existing = text("الأصناف الموجودة", 19, gold);
        existing.setPadding(0, 20, 0, 8);
        content.addView(existing);

        for (int i = 0; i < names.size(); i++) {
            final int index = i;

            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(10, 8, 10, 8);
            row.setBackground(shape(Color.WHITE, 12));

            TextView info = text(
                names.get(i) + " — " + prices.get(i)
                    + " ريال — الكمية: " + stock.get(i),
                14, dark
            );
            row.addView(info, new LinearLayout.LayoutParams(0, -2, 1));

            Button edit = button("تعديل");
            Button delete = button("حذف");
            row.addView(edit);
            row.addView(delete);

            LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, 3, 0, 3);
            content.addView(row, lp);

            edit.setOnClickListener(v -> editProduct(index));
            delete.setOnClickListener(v ->
                new android.app.AlertDialog.Builder(this)
                    .setTitle("حذف الصنف")
                    .setMessage("هل تريد حذف " + names.get(index) + "؟")
                    .setNegativeButton("إلغاء", null)
                    .setPositiveButton("حذف", (dialog, which) -> {
                        names.remove(index);
                        prices.remove(index);
                        stock.remove(index);
                        quantities.remove(index);
                        saveData();
                        showAddProduct();
                    }).show()
            );
        }
    }

    EditText field(String hint) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setTextSize(16);
        e.setTextColor(dark);
        e.setHint(hint);
        e.setPadding(12, 8, 12, 8);
        e.setBackground(shape(Color.WHITE, 12));

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 5, 0, 8);
        e.setLayoutParams(lp);
        return e;
    }

    void editProduct(int index) {
        LinearLayout form = column();
        form.setPadding(10, 4, 10, 4);

        EditText n = field("اسم الصنف");
        EditText p = field("السعر");
        EditText s = field("الكمية المتوفرة");

        n.setText(names.get(index));
        p.setText(String.valueOf(prices.get(index)));
        s.setText(String.valueOf(stock.get(index)));
        p.setInputType(2);
        s.setInputType(2);

        form.addView(n);
        form.addView(p);
        form.addView(s);

        new android.app.AlertDialog.Builder(this)
            .setTitle("تعديل الصنف")
            .setView(form)
            .setNegativeButton("إلغاء", null)
            .setPositiveButton("حفظ", (dialog, which) -> {
                try {
                    String newName = n.getText().toString().trim();
                    int newPrice = Integer.parseInt(
                        p.getText().toString().trim());
                    int newStock = Integer.parseInt(
                        s.getText().toString().trim());

                    if (newName.isEmpty() || newPrice < 0 || newStock < 0) {
                        Toast.makeText(this, "تحقق من البيانات",
                            Toast.LENGTH_SHORT).show();
                        return;
                    }

                    names.set(index, newName);
                    prices.set(index, newPrice);
                    stock.set(index, newStock);
                    saveData();
                    showAddProduct();
                } catch (Exception ex) {
                    Toast.makeText(this, "بيانات غير صحيحة",
                        Toast.LENGTH_SHORT).show();
                }
            }).show();
    }

    void loadData() {
        String saved = prefs.getString("products", null);

        if (saved == null) {
            for (int i = 0; i < initialNames.length; i++) {
                names.add(initialNames[i]);
                prices.add(initialPrices[i]);
                stock.add(100);
                quantities.add(0);
            }
            saveData();
            return;
        }

        try {
            JSONObject data = new JSONObject(saved);
            JSONArray ns = data.getJSONArray("names");
            JSONArray ps = data.getJSONArray("prices");
            JSONArray ss = data.getJSONArray("stock");

            for (int i = 0; i < ns.length(); i++) {
                names.add(ns.getString(i));
                prices.add(ps.getInt(i));
                stock.add(ss.getInt(i));
                quantities.add(0);
            }

            totalSales = data.optLong("sales", 0);
            invoiceCount = data.optInt("invoices", 0);
        } catch (Exception e) {
            names.clear();
            prices.clear();
            stock.clear();
            quantities.clear();

            for (int i = 0; i < initialNames.length; i++) {
                names.add(initialNames[i]);
                prices.add(initialPrices[i]);
                stock.add(100);
                quantities.add(0);
            }
        }
    }

    void saveData() {
        try {
            JSONObject data = new JSONObject();
            JSONArray ns = new JSONArray();
            JSONArray ps = new JSONArray();
            JSONArray ss = new JSONArray();

            for (int i = 0; i < names.size(); i++) {
                ns.put(names.get(i));
                ps.put(prices.get(i));
                ss.put(stock.get(i));
            }

            data.put("names", ns);
            data.put("prices", ps);
            data.put("stock", ss);
            data.put("sales", totalSales);
            data.put("invoices", invoiceCount);

            prefs.edit().putString("products", data.toString()).apply();
        } catch (Exception e) {
            Toast.makeText(this, "تعذر حفظ البيانات",
                Toast.LENGTH_SHORT).show();
        }
    }
}
