package com.colabear754.kbo_scraper.api.services

import com.colabear754.kbo_scraper.api.domain.GameInfo
import com.colabear754.kbo_scraper.api.domain.Team
import com.colabear754.kbo_scraper.api.dto.responses.CollectDataResponse
import com.colabear754.kbo_scraper.api.dto.responses.FindGameInfoResponse
import com.colabear754.kbo_scraper.api.repositories.GameInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class GameInfoDataService(
    private val gameInfoRepository: GameInfoRepository
) {
    @Transactional
    fun saveOrUpdateGameInfo(gameInfoList: List<GameInfo>): CollectDataResponse {
        val existingGamesMap = gameInfoRepository.findByGameKeyIn(gameInfoList.map { it.gameKey })
            .associateBy { it.gameKey }

        val (newGameList, gameListToUpdate) = gameInfoList.partition { existingGamesMap[it.gameKey] == null }
        gameInfoRepository.saveAll(newGameList)

        var modifiedCount = 0
        for (gameInfo in gameListToUpdate) {
            existingGamesMap[gameInfo.gameKey]?.run {
                if (update(gameInfo)) modifiedCount++
            }
        }

        return CollectDataResponse(gameInfoList.size, newGameList.size, modifiedCount)
    }

    fun findGameInfoByTeamAndDate(date: LocalDate, team: Team): List<FindGameInfoResponse> {
        return gameInfoRepository.findByDateAndTeam(date, team)
            .map(FindGameInfoResponse::from)
    }

    fun findGameInfoByGameKey(gameKey: String): FindGameInfoResponse? {
        return FindGameInfoResponse.from(gameInfoRepository.findByGameKey(gameKey))
    }
}