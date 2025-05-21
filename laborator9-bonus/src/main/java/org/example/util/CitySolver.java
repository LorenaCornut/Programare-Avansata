package org.example.util;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.example.entity.City;
import java.util.*;
import java.util.stream.Collectors;

public class CitySolver {
    public static List<City> findMatchingCities(List<City> allCities, int lowerBound, int upperBound) {
        List<City> validCities = allCities.stream()
                .filter(c -> c.getName() != null && !c.getName().isEmpty())
                .collect(Collectors.toList());

        Map<Character, List<City>> citiesByFirstLetter = validCities.stream()
                .collect(Collectors.groupingBy(
                        c -> Character.toUpperCase(c.getName().charAt(0)),
                        Collectors.toList()
                ));

        for (List<City> cityGroup : citiesByFirstLetter.values()) {
            if (cityGroup.size() < 2) continue;

            List<City> result = solveForGroup(cityGroup, lowerBound, upperBound);
            if (result != null && !result.isEmpty()) {
                return result;
            }
        }

        return Collections.emptyList();
    }

    private static List<City> solveForGroup(List<City> cityGroup, int lowerBound, int upperBound) {
        Model model = new Model("City Selection Problem");
        int n = cityGroup.size();

        IntVar[] selections = model.intVarArray("select", n, 0, 1);

        int[] populations = cityGroup.stream()
                .mapToInt(City::getPopulation)
                .toArray();

        IntVar totalPopulation = model.intVar("totalPop", lowerBound, upperBound);

        model.scalar(selections, populations, "=", totalPopulation).post();

        model.sum(selections, ">=", 2).post();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (cityGroup.get(i).getCountry().getId() == cityGroup.get(j).getCountry().getId()) {
                    model.arithm(selections[i], "+", selections[j], "<=", 1).post();
                }
            }
        }

        if (model.getSolver().solve()) {
            List<City> selectedCities = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (selections[i].getValue() == 1) {
                    selectedCities.add(cityGroup.get(i));
                }
            }
            return selectedCities;
        }

        return null;
    }
}